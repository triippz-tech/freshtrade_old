import React from 'react';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Settings from './settings/settings';
import Password from './password/password';
import AccountReservations from 'app/modules/account/reservations';

const Routes = ({ match }) => (
  <div>
    <ErrorBoundaryRoute path={`${match.url}/reservations`} component={AccountReservations} />
    <ErrorBoundaryRoute path={`${match.url}/settings`} component={Settings} />
    <ErrorBoundaryRoute path={`${match.url}/password`} component={Password} />
  </div>
);

export default Routes;
