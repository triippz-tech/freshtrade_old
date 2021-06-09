import React from 'react';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import Items from './items';
import ItemDetail from 'app/modules/items/item-detail';

const Routes = ({ match }) => (
  <div>
    <ErrorBoundaryRoute exact path={`${match.url}/detail/:id`} component={ItemDetail} />
    <ErrorBoundaryRoute exact path={`${match.url}/:slug`} component={Items} />
    <ErrorBoundaryRoute exact path={match.url} component={Items} />
  </div>
);

export default Routes;
