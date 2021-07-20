import React from 'react';

import { Dropdown, DropdownToggle, DropdownMenu } from 'mdbreact';
import defaultImage from 'app/assets/img/default-user.png';

// export const NavDropdown = props => (
//   <UncontrolledDropdown nav inNavbar id={props.id} data-cy={props['data-cy']}>
//     <DropdownToggle nav caret className="d-flex align-items-center">
//       <FontAwesomeIcon icon={props.icon} />
//       <span>{props.name}</span>
//     </DropdownToggle>
//     <DropdownMenu right style={props.style}>
//       {props.children}
//     </DropdownMenu>
//   </UncontrolledDropdown>
// );

export const NavDropdown = props => (
  <Dropdown id={props.id}>
    <DropdownToggle tag="menu" className="text-reset dropdown-toggle d-flex align-items-center hidden-arrow">
      {props.icon ? <i className={props.icon}>{` ${props.name}`}</i> : props.name}
    </DropdownToggle>
    <DropdownMenu right style={props.style}>
      {props.children}
    </DropdownMenu>
  </Dropdown>
);

export const NavDropdownUser = props => (
  <Dropdown id={props.id}>
    <DropdownToggle tag="usermenu" className="text-reset dropdown-toggle d-flex align-items-center hidden-arrow">
      <img src={props.image ? props.image : defaultImage} alt="USER" className="rounded-circle" height="22" loading="lazy" />
    </DropdownToggle>

    <DropdownMenu right style={props.style}>
      {props.children}
    </DropdownMenu>
  </Dropdown>
);
