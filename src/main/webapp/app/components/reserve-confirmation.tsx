import React from 'react';
import { Link } from 'react-router-dom';
import { Alert } from 'reactstrap';

export const ReserveConfirmation = ({ showAlert = false, isErr = false, message = '', reservationNumber = '', setAlert }) => {
  if (!showAlert) return null;
  return (
    <div className="d-block" id="reserveConfirmation">
      <Alert color={isErr ? 'danger' : 'success'} className="mb-4 mb-lg-5" role="alert" isOpen={showAlert}>
        <div className="d-flex align-items-center pr-3">
          <i className="fa fa-check-circle d-none d-sm-block w-3rem h-3rem svg-icon-light flex-shrink-0 mr-3" aria-hidden="true"></i>
          <p className="mb-0">
            {message} {!isErr && <Link to={`/reservations/${reservationNumber}`}>View Reservation</Link>}
            <br className="d-inline-block d-lg-none" />
          </p>
        </div>
        <button
          onClick={() =>
            setAlert({
              showAlert: false,
              isErr: false,
              message: '',
            })
          }
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
