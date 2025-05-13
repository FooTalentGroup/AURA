import React from 'react';

export interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  children: React.ReactNode;
  
}

/**
 * Generic modal backdrop + container
 */
export const Modal: React.FC<ModalProps> = ({ isOpen, onClose, children }) => {
  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div className="absolute inset-0" onClick={onClose} />
      <div className="relative bg-white rounded-2xl p-10 max-w-lg w-full shadow-lg min-h-[250px] flex flex-col justify-center">
        <button
          className="absolute top-4 right-4 text-gray-400 hover:text-gray-600"
          onClick={onClose}
        >
          ×
        </button>
        {children}
      </div>
    </div>
  );
};

export interface LoadingResultProps {
  loading: boolean;
  success: boolean;
  errorMessage?: string;
  message?: string;
  onSuccessAction?: { label: string; callback: () => void };
  onSecondaryAction?: { label: string; callback: () => void };
}

/**
 * Modal that shows:
 * - spinner when loading === true
 * - success view when success === true
 * - error view when errorMessage is provided
 */
export const LoadingResultModal: React.FC<LoadingResultProps> = ({
  loading,
  success,
  errorMessage,
  message = '¡Listo!',
  onSuccessAction,
  onSecondaryAction,
}) => {
  // Error state
  if (errorMessage && !loading) {
    return (
      <div className="text-center">
        {/* Error icon */}
        <div className="flex justify-center py-4">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-12 w-12 text-red-600"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            strokeWidth={3}
          >
            <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </div>
        <p className="text-lg font-medium text-red-600 mb-6">{errorMessage}</p>
        <div className="flex justify-center">
          {onSecondaryAction && (
            <button
              onClick={onSecondaryAction.callback}
              className="px-6 py-2 bg-gray-200 text-gray-700 rounded-full hover:bg-gray-300"
            >
              {onSecondaryAction.label}
            </button>
          )}
        </div>
      </div>
    );
  }

  return (
    <div className="text-center">
      {/* Loading state */}
      {loading && (
        <div className="flex justify-center py-8">
          <div className="w-10 h-10 border-4 border-gray-200 border-t-4 border-t-[#198038] rounded-full animate-spin" />
        </div>
      )}

      {/* Success state */}
      {!loading && success && (
        <>
          <div className="flex justify-center py-4">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-12 w-12 text-[#198038]"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              strokeWidth={3}
            >
              <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
            </svg>
          </div>
          <p className="text-lg font-medium mb-6">{message}</p>
          <div className="flex justify-center space-x-4">
            {onSecondaryAction && (
              <button
                onClick={onSecondaryAction.callback}
                className="px-6 py-2 border border-blue-400 text-blue-400 rounded-full hover:bg-blue-50"
              >
                {onSecondaryAction.label}
              </button>
            )}
            {onSuccessAction && (
              <button
                onClick={onSuccessAction.callback}
                className="px-6 py-2 bg-blue-400 text-white rounded-full hover:bg-blue-500"
              >
                {onSuccessAction.label}
              </button>
            )}
          </div>
        </>
      )}
    </div>
  );
};
