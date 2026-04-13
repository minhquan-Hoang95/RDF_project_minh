import { render, screen } from "@testing-library/react";
import ContentAnnotator from "./ContentAnnotator";
import { act } from "react-dom/test-utils";

// Mock the services and extension global APIs
jest.mock("../services/rdfServices", () => ({
  createAnnotation: jest.fn(),
  getCampaignById: jest.fn(),
}));

describe("ContentAnnotator Selection Preview", () => {
  const mockCampaign = { id: 1, name: "Test Campaign" };

  test("should display a preview when a text item is selected", async () => {
    // Mock the chrome/browser message response for getSelectedData
    const mockItem = {
      type: "text",
      value: "This is some selected text for testing.",
      position: { x: 10, y: 20 },
    };

    // Setting up the mock global browser/chrome object
    global.chrome = {
      runtime: {
        sendMessage: jest.fn().mockImplementation((message, callback) => {
          if (message.request === "getSelectedData") {
            // Check if it's being called with a callback (fallback) or returns a promise
            if (typeof callback === "function") {
              callback(mockItem);
            }
            return Promise.resolve(mockItem);
          }
        }),
      },
      tabs: {
        query: jest.fn().mockImplementation((options, callback) => {
          const result = [{ url: "http://example.com" }];
          if (typeof callback === "function") {
            callback(result);
          }
          return Promise.resolve(result);
        }),
      },
    };

    await act(async () => {
      render(<ContentAnnotator campaign={mockCampaign} />);
    });

    // Check for the preview label
    const previewLabel = screen.queryByText(/Selected Content Preview:/i);
    expect(previewLabel).toBeInTheDocument();

    // Check if the selected text is visible in the preview
    const previewValue = screen.getByText(mockItem.value);
    expect(previewValue).toBeInTheDocument();
  });
});
