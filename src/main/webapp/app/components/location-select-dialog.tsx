import React from 'react';
import { UserLocation } from 'app/shared/model/gis/user-location';
import { Button, Col, Form, FormGroup, Input, Label, Modal, ModalBody, ModalHeader, Row } from 'reactstrap';
import Select from 'react-select';
import { defaultValue, ICountry } from 'app/shared/model/country.model';
import DistanceSlider from 'app/components/distance-slider';

export interface LocationSelectDialog {
  currentLocation?: UserLocation;
  onLocationChange?: (newLocation) => void;
  getCurrentLocation?: () => void;
  isOpen: boolean;
  toggle: () => void;
  locationNotFound: boolean;
  countries: ReadonlyArray<ICountry>;
  searchCountry?: (postalCode: string, selectedCountry: ICountry, locationSearchCallback: (wasFound: boolean) => void) => void;
  onDistanceChange?: (value: number) => void;
}

const defaultProps: LocationSelectDialog = {
  countries: [],
  isOpen: false,
  locationNotFound: false,
  toggle(): void {},
};

const FormView = ({ location, onPostalCodeClick, onDistanceChange }) => (
  <Form>
    <FormGroup className="mb-1">
      <h6>Postal Code</h6>
      <Button color="link" className="w-100" onClick={() => onPostalCodeClick()}>
        <Row>
          <Col md="10" className="float-left w-100">
            <p>{`${location.city}, ${location.state ? location.state : location.countryCode} ${location.postal && location.postal}`}</p>
          </Col>
          <Col md="2" className="float-right">
            <i className="fa fa-chevron-right" aria-hidden="true"></i>
          </Col>
        </Row>
      </Button>
    </FormGroup>
    <hr />
    <FormGroup className="mb-1">
      <h6>Distance ({location.conversionType.toLowerCase()}.)</h6>
      <DistanceSlider
        onDistanceChange={onDistanceChange}
        value={location.distance}
        startingValue={location.distance}
        distanceType={`${location.conversionType.toLowerCase()}.`}
      />
    </FormGroup>
  </Form>
);

const LocationSelectView = ({
  locationName,
  onGetCurrentLocation,
  postalCodeValue,
  onChange,
  errState,
  selectedCountry,
  countries,
  onCountrySelect,
}) => (
  <>
    <Row>
      <Col className="text-center">
        <h6>Where are you searching?</h6>
      </Col>
    </Row>
    <Row>
      <Col className="text-center">
        <Button outline color="primary" onClick={() => onGetCurrentLocation()}>
          <i className="fa fa-map-marker" aria-hidden="true"></i>
          Get My Location
        </Button>
      </Col>
    </Row>
    <Row>
      <Col className="text-center">
        <br />
        <h6>Or</h6>
        <br />
      </Col>
    </Row>
    <Row>
      <Col></Col>
      <Col className="text-center">
        <Label for="postalCode">Postal Code</Label>
        <Input name="postalCode" className="text-center" value={postalCodeValue} onChange={onChange} placeholder="Postal Code" />
      </Col>
      <Col className="text-center">
        <Label for="countrySelect">Country</Label>
        <Select
          name="countrySelect"
          className="basic-single"
          classNamePrefix="select"
          value={selectedCountry}
          isClearable={true}
          isSearchable={true}
          onChange={onCountrySelect}
          options={countries}
        />
      </Col>
      <Col></Col>
    </Row>
    <Row>
      <Col className="text-center">{errState.isErr ? <h6 className="text-danger">{errState.errText}</h6> : <h6>{locationName}</h6>}</Col>
    </Row>
  </>
);

const currentLocationToCountry = (countries: ReadonlyArray<ICountry>, currentLocation: UserLocation): ICountry => {
  if (countries.length === 0) return defaultValue;

  const found = countries.find(function (country, index) {
    if (country.countryCode === currentLocation.countryCode) return true;
  });
  return found === undefined ? defaultValue : found;
};

export const LocationSelectDialog = (props: LocationSelectDialog) => {
  const [postalCode, setPostalCode] = React.useState(props.currentLocation.postal);
  const [isLocSearch, setIsLocSearch] = React.useState(false);
  const [errState, setErrState] = React.useState({
    isErr: props.locationNotFound,
    errText: '',
  });
  const [selectedCountry, setSelectedCountry] = React.useState<ICountry>(
    props.currentLocation ? currentLocationToCountry(props.countries, props.currentLocation) : defaultValue
  );

  React.useEffect(() => {
    setPostalCode(props.currentLocation.postal);
    setSelectedCountry(currentLocationToCountry(props.countries, props.currentLocation));
  }, [props.currentLocation]);

  const toggleDialog = () => {
    props.toggle();
    setIsLocSearch(false);
  };

  const onCountrySelect = val => {
    if (val === null) setSelectedCountry(defaultValue);
    else setSelectedCountry(val.country);
  };

  const locationSearchCallback = (wasFound: boolean) => {
    if (wasFound) {
      setIsLocSearch(false);
    } else {
      setErrState({
        isErr: true,
        errText: 'Could not find a locale with specified postal code and country',
      });
    }
  };

  const onApply = () => {
    if (postalCode === '' && selectedCountry.id === undefined) {
      setErrState({
        isErr: true,
        errText: 'Must enter a postal code and country',
      });
      return;
    } else if (postalCode === '') {
      setErrState({
        isErr: true,
        errText: 'Must enter a postal code',
      });
      return;
    } else if (selectedCountry.id === undefined) {
      setErrState({
        isErr: true,
        errText: 'Must enter a country',
      });
      return;
    } else {
      setErrState({ isErr: false, errText: '' });
    }
    props.searchCountry(postalCode, selectedCountry, locationSearchCallback);
  };

  const formatCountryList = () => {
    return props.countries.map(value => {
      return {
        value: value.countryCode,
        label: value.countryCode,
        country: value,
      };
    });
  };

  const formatCountry = (country: ICountry) => {
    /* eslint object-shorthand: 0 */
    return {
      label: country.countryCode,
      value: country.countryCode,
      country: country,
    };
  };

  return (
    <Modal isOpen={props.isOpen} toggle={toggleDialog}>
      <ModalHeader toggle={toggleDialog}>
        {isLocSearch ? (
          <>
            <Button color="link" onClick={() => setIsLocSearch(false)}>
              <i className="fa fa-chevron-left" aria-hidden="true"></i>
            </Button>
            Postal Code
          </>
        ) : (
          'Location'
        )}
      </ModalHeader>
      <ModalBody>
        {!isLocSearch ? (
          <FormView
            location={props.currentLocation}
            onPostalCodeClick={() => setIsLocSearch(true)}
            onDistanceChange={props.onDistanceChange}
          />
        ) : (
          <LocationSelectView
            countries={formatCountryList()}
            selectedCountry={formatCountry(selectedCountry)}
            onCountrySelect={onCountrySelect}
            errState={errState}
            locationName={
              !props.locationNotFound ? `${props.currentLocation.city}, ${props.currentLocation.state}` : 'Please enter a valid postal code'
            }
            onGetCurrentLocation={() => props.getCurrentLocation()}
            postalCodeValue={postalCode}
            onChange={e => setPostalCode(e.target.value)}
          />
        )}

        <br />
        <hr />

        <Row>
          <Col className="text-center">
            {!isLocSearch ? (
              <>
                <Button color="primary" onClick={toggleDialog}>
                  Confirm
                </Button>
              </>
            ) : (
              <>
                <Button className="w-75" color="primary" onClick={() => onApply()}>
                  Apply
                </Button>
              </>
            )}
          </Col>
        </Row>
      </ModalBody>
    </Modal>
  );
};

LocationSelectDialog.defaultProps = defaultProps;

export default LocationSelectDialog;
