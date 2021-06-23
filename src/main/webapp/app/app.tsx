import 'react-toastify/dist/ReactToastify.css';
import './app.scss';
import 'app/config/dayjs.ts';

import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { BrowserRouter as Router } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import { hot } from 'react-hot-loader';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';
import { getProfile } from 'app/shared/reducers/application-profile';
import { setLocale } from 'app/shared/reducers/locale';
import Footer from 'app/shared/layout/footer/footer';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import ErrorBoundary from 'app/shared/error/error-boundary';
import { AUTHORITIES } from 'app/config/constants';
import AppRoutes from 'app/routes';
import CustomNavbar from 'app/shared/layout/header/custom-navbar';
import { getFeaturedEntities } from 'app/entities/category/category.reducer';
import { changeDistance, findZipCode, getUserLocation } from 'app/shared/reducers/user-location.reducer';
import { LocationSelectDialog } from 'app/components/location-select-dialog';
import { getEntitiesNoPage } from 'app/entities/country/country.reducer';
import { ICountry } from 'app/shared/model/country.model';

const baseHref = document.querySelector('base').getAttribute('href').replace(/\/$/, '');

export interface IAppProps extends StateProps, DispatchProps {}

export const App = (props: IAppProps) => {
  const [isLocationOpen, setIsLocationOpen] = React.useState(false);

  useEffect(() => {
    props.getSession();
    props.getProfile();
    props.getFeaturedEntities();
    props.getUserLocation();
  }, []);

  useEffect(() => {
    if (isLocationOpen) props.getEntitiesNoPage();
  }, [isLocationOpen]);

  const openLocationDialog = () => {
    setIsLocationOpen(!isLocationOpen);
  };

  const searchCountry = (postalCode: string, country: ICountry, locationSearchCallback: (wasFound: boolean) => void) => {
    props.findZipCode(postalCode, country.countryCode, locationSearchCallback);
  };

  const paddingTop = '10px';
  return (
    <Router basename={baseHref}>
      <div className="app-container" style={{ paddingTop }}>
        <ToastContainer position={toast.POSITION.TOP_LEFT} className="toastify-container" toastClassName="toastify-toast" />
        <ErrorBoundary>
          <CustomNavbar
            locationDistance={`${props.userLocation.distance} ${props.userLocation.conversionType.toLowerCase()}.`}
            locationName={`${props.userLocation.city}, ${
              props.userLocation.state ? props.userLocation.state : props.userLocation.countryName
            }`}
            onLocationClick={() => setIsLocationOpen(true)}
            isAuthenticated={props.isAuthenticated}
            isAdmin={props.isAdmin}
            currentLocale={props.currentLocale}
            onLocaleChange={props.setLocale}
            ribbonEnv={props.ribbonEnv}
            isInProduction={props.isInProduction}
            isOpenAPIEnabled={props.isOpenAPIEnabled}
            categories={props.categories}
          />
        </ErrorBoundary>
        <div className="container-fluid view-container" id="app-view-container">
          <ErrorBoundary>
            <LocationSelectDialog
              searchCountry={searchCountry}
              getCurrentLocation={() => props.getUserLocation()}
              locationNotFound={false}
              currentLocation={props.userLocation}
              isOpen={isLocationOpen}
              toggle={() => openLocationDialog()}
              countries={props.countries}
              onDistanceChange={props.changeDistance}
            />
            <AppRoutes />
          </ErrorBoundary>
        </div>
        <Footer />
      </div>
    </Router>
  );
};

const mapStateToProps = ({ authentication, applicationProfile, locale, category, userLocation, country }: IRootState) => ({
  currentLocale: locale.currentLocale,
  isAuthenticated: authentication.isAuthenticated,
  isAdmin: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.ADMIN]),
  ribbonEnv: applicationProfile.ribbonEnv,
  isInProduction: applicationProfile.inProduction,
  isOpenAPIEnabled: applicationProfile.isOpenAPIEnabled,
  categories: category.headerEntities,
  userLocation: userLocation.userLocation,
  countries: country.entities,
});

const mapDispatchToProps = {
  setLocale,
  getSession,
  getProfile,
  getFeaturedEntities,
  getUserLocation,
  getEntitiesNoPage,
  findZipCode,
  changeDistance,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(hot(module)(App));
