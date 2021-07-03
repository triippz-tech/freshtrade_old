import React from 'react';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import SellerDashboard from 'app/modules/seller/seller-dashboard';
import SellerMessages from 'app/modules/seller/seller-messages';
import SellerItemUpdate from 'app/modules/seller/seller-item-update';
import SellerItems from 'app/modules/seller/seller-items';
import SellerReservations from 'app/modules/seller/seller-reservations';
import { Switch } from 'react-router-dom';
import ItemDeleteDialog from 'app/entities/item/item-delete-dialog';
import SellerItemNew from 'app/modules/seller/seller-item-new';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={match.url} component={SellerDashboard} />
      <ErrorBoundaryRoute exact path={`${match.url}/messages`} component={SellerMessages} />
      <ErrorBoundaryRoute exact path={`${match.url}/reservations`} component={SellerReservations} />
      <ErrorBoundaryRoute exact path={`${match.url}/items`} component={SellerItems} />
      <ErrorBoundaryRoute exact path={`${match.url}/items/new`} component={SellerItemNew} />
      <ErrorBoundaryRoute exact path={`${match.url}/items/:id/edit`} component={SellerItemUpdate} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/items/:id/delete`} component={ItemDeleteDialog} />
  </>
);

export default Routes;
