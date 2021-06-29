import React from 'react';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown, NavDropdownUser } from './menu-components';
import { DropdownItem } from 'mdbreact';
import { Link } from 'react-router-dom';

const accountMenuItemsAuthenticated = (
  <>
    <Link to="/account/settings">
      <DropdownItem>
        <Translate contentKey="global.menu.account.settings">Settings</Translate>
      </DropdownItem>
    </Link>
    <Link to="/account/password">
      <DropdownItem>
        <Translate contentKey="global.menu.account.password">Password</Translate>
      </DropdownItem>
    </Link>
    <Link to="/logout">
      <DropdownItem>
        <Translate contentKey="global.menu.account.logout">Sign out</Translate>
      </DropdownItem>
    </Link>
  </>
);

export const AccountMenuItems = ({ location }) => {
  return (
    <>
      <Link
        to={{
          pathname: '/login',
          state: location.pathname,
        }}
      >
        <DropdownItem id="login-item">
          <Translate contentKey="global.menu.account.login">Sign in</Translate>
        </DropdownItem>
      </Link>
      <Link to="/account/register">
        <DropdownItem>
          <Translate contentKey="global.menu.account.register">Register</Translate>
        </DropdownItem>
      </Link>
    </>
  );
};

export const AccountMenu = ({ location, isAuthenticated = false, image = undefined }) => (
  <NavDropdownUser name={translate('global.menu.account.main')} id="account-menu" image={image ? image : undefined}>
    {isAuthenticated ? accountMenuItemsAuthenticated : <AccountMenuItems location={location} />}
  </NavDropdownUser>
);

export default AccountMenu;
