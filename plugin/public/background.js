let selectedData = { type: null, value: "", position: { x: 0, y: 0 } };

chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.type && message.position) {
    selectedData = {
      type: message.type,
      value: message.value,
      position: message.position,
    };
  }

  if (message.request === "getSelectedData") {
    sendResponse(selectedData);
  }

  if (message.type === "open") {
    chrome.tabs.create({ url: message.url }, (tab) => {
      chrome.scripting.executeScript({
        target: { tabId: tab.id },
        func: (y) => window.scrollBy(0, y - 50 > 0 ? y - 50 : 0),
        args: [message.y],
      });

      chrome.scripting.executeScript({
        target: { tabId: tab.id },
        func: (text) => {
          const highlightText = (text) => {
            const regex = new RegExp(text, "gi");

            const walk = (node) => {
              if (node.nodeType === 3) {
                const match = node.nodeValue.match(regex);
                if (match) {
                  const span = document.createElement("span");
                  span.innerHTML = node.nodeValue.replace(
                    regex,
                    (match) =>
                      `<span style="background-color: yellow; color: black;">${match}</span>`
                  );
                  node.parentNode.replaceChild(span, node);
                }
              } else if (
                node.nodeType === 1 &&
                node.nodeName !== "SCRIPT" &&
                node.nodeName !== "STYLE"
              ) {
                for (let i = 0; i < node.childNodes.length; i++) {
                  walk(node.childNodes[i]);
                }
              }
            };

            walk(document.body);
          };

          highlightText(text);
        },
        args: [message.text],
      });
    });
  }

  return true;
});
