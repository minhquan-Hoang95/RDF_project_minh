const browserObj = typeof browser !== "undefined" ? browser : chrome;

// Event listener for text selection
const handleMouseUp = (e) => {
  if (!e.isTrusted || typeof e.pageX !== "number") return;

  const selectedText = window.getSelection().toString();

  // Handling text selection
  if (selectedText.length > 0) {
    const x = e.pageX;
    const y = e.pageY;
    console.log(
      `Selected text: '${selectedText}' at page position (x: ${x}, y: ${y})`
    );
    browserObj.runtime.sendMessage({
      type: "text",
      value: selectedText,
      position: { x: x, y: y },
    });
  }
};

document.addEventListener("mouseup", handleMouseUp);

// Event listener for image click
const handleClick = (e) => {
  if (!e.isTrusted || typeof e.pageX !== "number") return;

  // Check if the clicked element is an image
  if (e.target.tagName === "IMG") {
    const imgSrc = e.target.src;
    const x = e.pageX;
    const y = e.pageY;
    console.log(
      `Selected image: ${imgSrc} at page position (x: ${x}, y: ${y})`
    );
    browserObj.runtime.sendMessage({
      type: "image",
      value: imgSrc,
      position: { x: x, y: y },
    });

    // Prevent default to stop any other actions that might happen on image click
    e.preventDefault();
  }
};

document.addEventListener("click", handleClick, true); // Use capture phase to handle the event early

// Dynamic content support: MutationObserver to re-attach or handle new elements
const observer = new MutationObserver((mutations) => {
  mutations.forEach((mutation) => {
    if (mutation.addedNodes.length) {
      console.log("New dynamic content detected");
      // In a more complex plugin, we might re-scan or re-initialize here
    }
  });
});

observer.observe(document.body, {
  childList: true,
  subtree: true,
});
