function getSvg (code) { // eslint-disable-line no-unused-vars
  let svg = `<svg xmlns="http://www.w3.org/2000/svg" width="190" height="270">
  <path d="M 0 258 L 190 258" fill="none" stroke="#157878" stroke-width="24"/>
  <path d="M 30 246 L 30 24" fill="none" stroke="#157878" stroke-width="24"/>
  <path d="M 18 12 L 156 12" fill="none" stroke="#157878" stroke-width="24"/>
  <path d="M 30 80 L 98 12" fill="none" stroke="#157878" stroke-width="12"/>`;
  if (code >= 1) {
    svg += '<path d="M 112 24 L 112 50" fill="none" stroke="#000" stroke-width="4"/>';
  }
  if (code >= 2) {
    svg += '<path d="M 134 72 A 22 22 0 1 1 133.999 71.978" fill="none" stroke="#000" stroke-width="4"/>';
  }
  if (code >= 3) {
    svg += '<path d="M 112 94 L 112 160" fill="none" stroke="#000" stroke-width="4"/>';
  }
  if (code >= 4) {
    svg += '<path d="M 112 105 L 80 165" fill="none" stroke="#000" stroke-width="4"/>';
  }
  if (code >= 5) {
    svg += '<path d="M 112 105 L 142 165" fill="none" stroke="#000" stroke-width="4"/>';
  }
  if (code >= 6) {
    svg += '<path d="M 112 160 L 80 220" fill="none" stroke="#000" stroke-width="4"/>';
  }
  if (code >= 7) {
    svg += '<path d="M 112 160 L 142 220" fill="none" stroke="#000" stroke-width="4"/>';
  }
  if (code >= 8) {
    svg += `<path d="M 108 65 A 3 3 0 1 1 107.999 64.997 L 122 65 A 3 3 0 1 1 121.999 64.997" fill="#000"/>
  <path d="M 119 84 A 7 7 0 1 0 105 84" fill="none" stroke="#000" stroke-width="4"/>`;
  }
  return `data:image/svg+xml;base64,${window.btoa(svg + '</svg>')}`;
}
