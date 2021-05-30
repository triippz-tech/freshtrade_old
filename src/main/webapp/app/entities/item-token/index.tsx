import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ItemToken from './item-token';
import ItemTokenDetail from './item-token-detail';
import ItemTokenUpdate from './item-token-update';
import ItemTokenDeleteDialog from './item-token-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ItemTokenUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ItemTokenUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ItemTokenDetail} />
      <ErrorBoundaryRoute path={match.url} component={ItemToken} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ItemTokenDeleteDialog} />
  </>
);

export default Routes;
