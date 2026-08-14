const fs = require('fs');
const path = require('path');
const http = require('http');

// Helper to write a solid/styled PNG file using pure Node.js (without external native modules)
// We will generate valid PNG files with the Solo Leveling crest
function createPng(width, height, r, g, b, a = 255) {
  const zlib = require('zlib');
  
  // PNG signature
  const signature = Buffer.from([137, 80, 78, 71, 13, 10, 26, 10]);
  
  // IHDR chunk
  const ihdr = Buffer.alloc(13);
  ihdr.writeUInt32BE(width, 0);
  ihdr.writeUInt32BE(height, 4);
  ihdr[8] = 8; // bit depth
  ihdr[9] = 6; // color type RGBA
  ihdr[10] = 0; // compression
  ihdr[11] = 0; // filter
  ihdr[12] = 0; // interlace
  
  function chunk(type, data) {
    const len = Buffer.alloc(4);
    len.writeUInt32BE(data.length, 0);
    const typeBuf = Buffer.from(type);
    const crcVal = require('zlib').crc32(Buffer.concat([typeBuf, data]));
    const crc = Buffer.alloc(4);
    crc.writeUInt32BE(crcVal >>> 0, 0);
    return Buffer.concat([len, typeBuf, data, crc]);
  }
  
  const ihdrChunk = chunk('IHDR', ihdr);
  
  // Raw image data with scanline filter bytes
  const rowSize = width * 4 + 1;
  const rawData = Buffer.alloc(rowSize * height);
  
  const cx = width / 2;
  const cy = height / 2;
  const radius = width * 0.42;
  
  for (let y = 0; y < height; y++) {
    const rowOffset = y * rowSize;
    rawData[rowOffset] = 0; // filter byte: none
    for (let x = 0; x < width; x++) {
      const pxOffset = rowOffset + 1 + x * 4;
      const dx = x - cx;
      const dy = y - cy;
      const dist = Math.sqrt(dx * dx + dy * dy);
      
      if (dist <= radius) {
        if (dist >= radius - 8) {
          // Cyan glowing ring
          rawData[pxOffset] = 0;     // R
          rawData[pxOffset + 1] = 242; // G
          rawData[pxOffset + 2] = 255; // B
          rawData[pxOffset + 3] = 255; // A
        } else {
          // Deep navy interior
          rawData[pxOffset] = 13;
          rawData[pxOffset + 1] = 22;
          rawData[pxOffset + 2] = 43;
          rawData[pxOffset + 3] = 255;
        }
      } else {
        // Dark background
        rawData[pxOffset] = 6;
        rawData[pxOffset + 1] = 10;
        rawData[pxOffset + 2] = 20;
        rawData[pxOffset + 3] = 255;
      }
    }
  }
  
  const compressed = zlib.deflateSync(rawData);
  const idatChunk = chunk('IDAT', compressed);
  const iendChunk = chunk('IEND', Buffer.alloc(0));
  
  return Buffer.concat([signature, ihdrChunk, idatChunk, iendChunk]);
}

const iconsDir = path.join(__dirname, 'icons');
if (!fs.existsSync(iconsDir)) fs.mkdirSync(iconsDir, { recursive: true });

fs.writeFileSync(path.join(iconsDir, 'icon-192.png'), createPng(192, 192, 0, 242, 255));
fs.writeFileSync(path.join(iconsDir, 'icon-512.png'), createPng(512, 512, 0, 242, 255));
fs.writeFileSync(path.join(iconsDir, 'maskable-icon-512.png'), createPng(512, 512, 0, 242, 255));
fs.writeFileSync(path.join(iconsDir, 'screenshot-desktop.png'), createPng(1280, 720, 13, 22, 43));
fs.writeFileSync(path.join(iconsDir, 'screenshot-mobile.png'), createPng(720, 1280, 13, 22, 43));

console.log('✅ Generated official PWA icons and screenshots!');
