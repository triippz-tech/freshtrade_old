import React from 'react';
import { ICategory } from 'app/shared/model/category.model';
import { MDBNavbar, MDBNavbarNav, NavItem } from 'mdbreact';
import { Link } from 'react-router-dom';

export interface CategoriesHeaderProps {
  categories: ReadonlyArray<ICategory>;
}

export const CategoriesHeader = (props: CategoriesHeaderProps) => {
  return (
    <MDBNavbar color="white" expand="xs" light>
      <MDBNavbarNav>
        <NavItem className="text-sm">
          <Link to={`/items`}>All</Link>
        </NavItem>
        {props.categories.map((category, idx) => {
          return (
            <NavItem key={`category-${idx}`} className="text-sm">
              <Link to={`/items/${category.slug}`}>{category.name}</Link>
            </NavItem>
          );
        })}
      </MDBNavbarNav>
    </MDBNavbar>
  );
};
export default CategoriesHeader;
