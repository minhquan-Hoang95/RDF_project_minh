import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import ContentAnnotator from "./ContentAnnotator";
import { act } from "react-dom/test-utils";
import * as rdfServices from "../services/rdfServices";

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
      itemValue: "This is some selected text for testing.",
      position: { x: 10, y: 20 },
    };

    // Setting up the mock global browser/chrome object
    const mockRuntime = {
      sendMessage: jest.fn().mockImplementation((message, callback) => {
        if (message.request === "getSelectedData") {
          if (typeof callback === "function") {
            callback(mockItem);
          }
          return Promise.resolve(mockItem);
        }
      }),
    };

    global.chrome = {
      runtime: mockRuntime,
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

    window.chrome = global.chrome;

    await act(async () => {
      render(<ContentAnnotator campaign={mockCampaign} />);
    });

    // Check for the preview label
    const previewLabel = screen.queryByText(/Selected Content Preview:/i);
    expect(previewLabel).toBeInTheDocument();

    // Check if the selected text is visible in the preview
    // Using a partial match or function since we added quotes
    const previewValue = screen.getByText((content) => content.includes(mockItem.itemValue));
    expect(previewValue).toBeInTheDocument();
  });

  test("should include character count in description when submitting text annotation", async () => {
    const mockItem = {
      type: "text",
      itemValue: "Hello world", // 11 characters
      position: { x: 10, y: 20 },
    };

    global.chrome = {
      runtime: {
        sendMessage: jest.fn().mockImplementation((message, callback) => {
          if (message.request === "getSelectedData") {
            if (typeof callback === "function") callback(mockItem);
            return Promise.resolve(mockItem);
          }
        }),
      },
      tabs: {
        query: jest.fn().mockImplementation((options, callback) => {
          const result = [{ url: "http://example.com" }];
          if (typeof callback === "function") callback(result);
          return Promise.resolve(result);
        }),
      },
    };

    window.chrome = global.chrome;

    rdfServices.createAnnotation.mockResolvedValue({ data: {} });

    await act(async () => {
      render(<ContentAnnotator campaign={mockCampaign} />);
    });

    // Wait for the preview to appear to ensure state has updated
    await waitFor(() => {
      expect(screen.getByText((content) => content.includes(mockItem.itemValue))).toBeInTheDocument();
    });

    // Fill in description
    const descriptionInput = screen.getByLabelText(/Description:/i);
    fireEvent.change(descriptionInput, { target: { value: "My annotation" } });

    // Submit
    const submitButton = screen.getByText(/Submit/i);
    
    // Create the expected description string to check in logs if needed
    const expectedDescription = "My annotation (Selection word count: 2, char count: 11)";

    await act(async () => {
      fireEvent.click(submitButton);
    });

    await waitFor(() => {
      const calls = rdfServices.createAnnotation.mock.calls;
      const matchingCall = calls.find(call => 
        call[0] && call[0].description && call[0].description.includes("char count: 11")
      );
      expect(matchingCall).toBeDefined();
    }, { timeout: 3000 });
  });
});
