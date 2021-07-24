import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import AccountReservations from 'app/modules/account/reservations/account-reservations';
import RedeemReservation from 'app/modules/account/reservations/redeem-reservation';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute path={`${match.url}/redeem/:reservationNumber`} component={RedeemReservation} />
      <ErrorBoundaryRoute path={match.url} component={AccountReservations} />
    </Switch>
  </>
);

export default Routes;
