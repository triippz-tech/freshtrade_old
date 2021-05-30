import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TradeEvent from './trade-event';
import TradeEventDetail from './trade-event-detail';
import TradeEventUpdate from './trade-event-update';
import TradeEventDeleteDialog from './trade-event-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TradeEventUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TradeEventUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TradeEventDetail} />
      <ErrorBoundaryRoute path={match.url} component={TradeEvent} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TradeEventDeleteDialog} />
  </>
);

export default Routes;
