import React from 'react';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';
import { DropdownItem } from 'mdbreact';
import { Link } from 'react-router-dom';

export const SellerMenu = props => (
  <NavDropdown
    icon="fas fa-dollar-sign"
    name={translate('global.menu.seller.main')}
    id="seller-menu"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <Link to="/seller">
      <DropdownItem>
        <Translate contentKey="global.menu.seller.dashboard">Dashboard</Translate>
      </DropdownItem>
    </Link>
    <Link to="/seller/items">
      <DropdownItem>
        <Translate contentKey="global.menu.seller.post">Items</Translate>
      </DropdownItem>
    </Link>
    <Link to="/seller/reservations">
      <DropdownItem>
        <Translate contentKey="global.menu.seller.reservations">Reservations</Translate>
      </DropdownItem>
    </Link>
    <Link to="/seller/messages">
      <DropdownItem>
        <Translate contentKey="global.menu.seller.messages">Messages</Translate>
      </DropdownItem>
    </Link>
  </NavDropdown>
);

export default SellerMenu;
