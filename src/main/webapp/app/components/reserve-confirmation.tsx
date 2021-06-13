import React from 'react';
import { Alert } from 'reactstrap';

export const ReserveConfirmation = ({ productName, isShowing, setAlert }) => {
  return (
    <div className="d-block" id="reserveConfirmation">
      <Alert color="success" className="mb-4 mb-lg-5" role="alert" isOpen={isShowing}>
        <div className="d-flex align-items-center pr-3">
          <i className="fa fa-check-circle d-none d-sm-block w-3rem h-3rem svg-icon-light flex-shrink-0 mr-3" aria-hidden="true"></i>
          <p className="mb-0">
            {productName} has been reserved.
            <br className="d-inline-block d-lg-none" />
          </p>
        </div>
        <button
          onClick={() => setAlert(false)}
          className="close close-absolute close-centered opacity-10 text-inherit"
          type="button"
          aria-label="Close"
        >
          <i className="fa fa-times-circle w-2rem h-2rem" aria-hidden="true"></i>
        </button>
      </Alert>
    </div>
  );
};

export default ReserveConfirmation;
