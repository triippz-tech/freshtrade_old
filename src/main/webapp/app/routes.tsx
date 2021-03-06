import React from 'react';
import { Switch, useLocation } from 'react-router-dom';
import Loadable from 'react-loadable';

import Login from 'app/modules/login/login';
import Register from 'app/modules/account/register/register';
import Activate from 'app/modules/account/activate/activate';
import PasswordResetInit from 'app/modules/account/password-reset/init/password-reset-init';
import PasswordResetFinish from 'app/modules/account/password-reset/finish/password-reset-finish';
import Logout from 'app/modules/login/logout';
import Home from 'app/modules/home/home';
import Entities from 'app/entities';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import PageNotFound from 'app/shared/error/page-not-found';
import { AUTHORITIES } from 'app/config/constants';
import { sendActivity } from 'app/config/websocket-middleware';
import PrivacyPolicy from 'app/modules/static-pages/privacy-policy';
import ToCPage from 'app/modules/static-pages/terms-and-conditions';
import About from 'app/modules/static-pages/about';

const Account = Loadable({
  loader: () => import(/* webpackChunkName: "account" */ 'app/modules/account'),
  loading: () => <div>loading ...</div>,
});

const Admin = Loadable({
  loader: () => import(/* webpackChunkName: "administration" */ 'app/modules/administration'),
  loading: () => <div>loading ...</div>,
});

const Items = Loadable({
  loader: () => import(/* webpackChunkName: "items" */ 'app/modules/items'),
  loading: () => <div>loading ...</div>,
});

const Seller = Loadable({
  loader: () => import(/* webpackChunkName: "seller" */ 'app/modules/seller'),
  loading: () => <div>loading ...</div>,
});

const Routes = () => {
  const location = useLocation();
  React.useEffect(() => {
    sendActivity(location.pathname);
  }, [location]);
  return (
    <div className="view-routes">
      <Switch>
        <ErrorBoundaryRoute path="/login" component={Login} />
        <ErrorBoundaryRoute path="/terms" component={ToCPage} />
        <ErrorBoundaryRoute path="/privacy-policy" component={PrivacyPolicy} />
        <ErrorBoundaryRoute path="/about" component={About} />
        <ErrorBoundaryRoute path="/logout" component={Logout} />
        <ErrorBoundaryRoute path="/items" component={Items} />
        <ErrorBoundaryRoute path="/account/register" component={Register} />
        <ErrorBoundaryRoute path="/account/activate/:key?" component={Activate} />
        <ErrorBoundaryRoute path="/account/reset/request" component={PasswordResetInit} />
        <ErrorBoundaryRoute path="/account/reset/finish/:key?" component={PasswordResetFinish} />
        <PrivateRoute path="/seller" component={Seller} hasAnyAuthorities={[AUTHORITIES.SELLER]} />
        <PrivateRoute path="/admin" component={Admin} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
        <PrivateRoute path="/account" component={Account} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.BUYER]} />
        <ErrorBoundaryRoute path="/" exact component={Home} />
        <PrivateRoute path="/" component={Entities} hasAnyAuthorities={[AUTHORITIES.BUYER]} />
        <ErrorBoundaryRoute component={PageNotFound} />
      </Switch>
    </div>
  );
};

export default Routes;
