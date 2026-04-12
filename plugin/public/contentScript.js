// Event listener for text selection
document.addEventListener("mouseup", (e) => {
  if (!e.isTrusted || typeof e.pageX !== "number") return;

  const selectedText = window.getSelection().toString();

  // Handling text selection
  if (selectedText.length > 0) {
    const x = e.pageX;
    const y = e.pageY;
    console.log(
      `Selected text: '${selectedText}' at page position (x: ${x}, y: ${y})`
    );
    chrome.runtime.sendMessage({
      type: "text",
      value: selectedText,
      position: { x: x, y: y },
    });
  }
});

// Event listener for image click
document.addEventListener(
  "click",
  (e) => {
    if (!e.isTrusted || typeof e.pageX !== "number") return;

    // Check if the clicked element is an image
    if (e.target.tagName === "IMG") {
      const imgSrc = e.target.src;
      const x = e.pageX;
      const y = e.pageY;
      console.log(
        `Selected image: ${imgSrc} at page position (x: ${x}, y: ${y})`
      );
      chrome.runtime.sendMessage({
        type: "image",
        value: imgSrc,
        position: { x: x, y: y },
      });

      // Prevent default to stop any other actions that might happen on image click
      e.preventDefault();
    }
  },
  true
); // Use capture phase to handle the event early
