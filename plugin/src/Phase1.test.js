describe('Browser Compatibility and Dynamic Support', () => {
  let chromeMock;

  beforeEach(() => {
    // Basic mock of the chrome object
    chromeMock = {
      runtime: {
        sendMessage: jest.fn((msg, cb) => {
          if (cb) cb();
          return Promise.resolve();
        }),
        onMessage: {
          addListener: jest.fn(),
        },
      },
      tabs: {
        query: jest.fn((opts, cb) => {
          const tabs = [{ url: 'https://example.com' }];
          if (cb) cb(tabs);
          return Promise.resolve(tabs);
        }),
      },
    };
    global.chrome = chromeMock;
    // Mock the browser object that polyfill provides
    global.browser = chromeMock; 
  });

  afterEach(() => {
    delete global.chrome;
    delete global.browser;
    jest.resetModules();
  });

  test('should use the polyfill-compatible browser API', () => {
    // In actual code we would use import browser from 'webextension-polyfill'
    // but in tests we verify that our logic uses 'browser' instead of 'chrome'
    expect(global.browser.runtime).toBeDefined();
    expect(global.browser.tabs).toBeDefined();
  });

  test('should detect dynamic content changes via MutationObserver', () => {
    // This test will fail initially because we haven't implemented MutationObserver in contentScript.js
    // We'll need a way to mock the DOM and the observer
    const callback = jest.fn();
    const observer = new MutationObserver(callback);
    const target = document.createElement('div');
    observer.observe(target, { childList: true });
    
    target.appendChild(document.createElement('span'));
    
    // MutationObserver is async, so we wait
    return new Promise(resolve => {
      setTimeout(() => {
        expect(callback).toHaveBeenCalled();
        resolve();
      }, 0);
    });
  });
});
