import React from 'react';
import { ICategory } from 'app/shared/model/category.model';
import { MDBLink, MDBNavbar, MDBNavbarNav, NavItem } from 'mdbreact';

export interface CategoriesHeaderProps {
  categories: ReadonlyArray<ICategory>;
}

export const CategoriesHeader: React.FC<CategoriesHeaderProps> = props => (
  <MDBNavbar color="white" expand="sm" light>
    <MDBNavbarNav className="">
      {props.categories.map((category, idx) => {
        return (
          <NavItem key={`category-${idx}`}>
            <MDBLink to={`/items/${category.slug}`}>{category.name}</MDBLink>
          </NavItem>
        );
      })}
    </MDBNavbarNav>
  </MDBNavbar>
);
export default CategoriesHeader;
