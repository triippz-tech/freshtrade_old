import './header.scss';

import React, { useState } from 'react';
import { Translate, Storage } from 'react-jhipster';
import LoadingBar from 'react-redux-loading-bar';
import { Jumbotron } from 'app/shared/layout/header/jumbotron';
import { ICategory } from 'app/shared/model/category.model';
import CategoriesHeader from 'app/shared/layout/header/categories-header';

export interface INavbarProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
  onLocaleChange: (langKey: string) => void;
  categories: ReadonlyArray<ICategory>;
}

const CustomNavbar = (props: INavbarProps) => {
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    props.onLocaleChange(langKey);
  };

  const renderDevRibbon = () =>
    props.isInProduction === false ? (
      <div className="ribbon dev">
        <a href="">
          <Translate contentKey={`global.ribbon.${props.ribbonEnv}`} />
        </a>
      </div>
    ) : null;

  const toggleMenu = () => setMenuOpen(!menuOpen);

  /* jhipster-needle-add-element-to-menu - JHipster will add new menu items here */

  return (
    <div id="app-header">
      {renderDevRibbon()}
      <LoadingBar className="loading-bar" />
      <Jumbotron
        data-cy="navbar"
        locationName={'Lancaster, PA'}
        isAdmin={props.isAdmin}
        isAuthenticated={props.isAuthenticated}
        onLocationClick={() => console.log('location click')}
        isOpenAPIEnabled={props.isOpenAPIEnabled}
        currentLocale={props.currentLocale}
        handleLocaleChange={handleLocaleChange}
      />

      <CategoriesHeader categories={props.categories} />
    </div>
  );
};

export default CustomNavbar;