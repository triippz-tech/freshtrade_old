import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';
import { defaultValue, UserLocation } from 'app/shared/model/gis/user-location';
import axios from 'axios';
import _, { Dictionary } from 'lodash';
import { LocaleInformation, ZipCodeResponse } from 'app/shared/model/gis/zipcode-response.model';
import { useLocation } from 'react-router-dom';

export const ACTION_TYPES = {
  GET_LOCATION: 'location/GET_LOCATION',
  SEARCH_ZIPCODE: 'location/SEARCH_ZIPCODE',
  CHANGE_LOCATION: 'location/CHANGE_LOCATION',
  CHANGE_DISTANCE: 'location/CHANGE_DISTANCE',
};

const initialState = {
  loading: false,
  userLocation: defaultValue,
};

export type UserLocationState = Readonly<typeof initialState>;

// Reducer

export default (state: UserLocationState = initialState, action): UserLocationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.CHANGE_LOCATION):
    case REQUEST(ACTION_TYPES.CHANGE_DISTANCE):
    case REQUEST(ACTION_TYPES.SEARCH_ZIPCODE):
    case REQUEST(ACTION_TYPES.GET_LOCATION):
      return {
        ...state,
        loading: true,
      };
    case FAILURE(ACTION_TYPES.CHANGE_DISTANCE):
    case FAILURE(ACTION_TYPES.CHANGE_LOCATION):
    case FAILURE(ACTION_TYPES.SEARCH_ZIPCODE):
      return {
        ...state,
        loading: false,
      };
    case SUCCESS(ACTION_TYPES.CHANGE_DISTANCE):
      return {
        ...state,
        loading: false,
        userLocation: {
          ...state.userLocation,
          distance: action.payload,
        },
      };
    case SUCCESS(ACTION_TYPES.CHANGE_LOCATION): {
      return {
        ...state,
        loading: false,
      };
    }
    case SUCCESS(ACTION_TYPES.GET_LOCATION): {
      return {
        ...state,
        loading: false,
        userLocation: {
          ..._.mapKeys(action.payload.data, (value, key) => _.camelCase(key)),
          distance: 25,
          conversionType: action.payload.data.country_code === 'US' ? 'MI' : 'KM',
        },
      };
    }
    case SUCCESS(ACTION_TYPES.SEARCH_ZIPCODE): {
      return {
        ...state,
        loading: false,
        userLocation: action.payload,
      };
    }

    default:
      return state;
  }
};

const geoLocationDbAPI = axios.create({
  baseURL: 'https://geolocation-db.com/',
});

const zipcodebaseAPI = axios.create({
  baseURL: 'https://app.zipcodebase.com/api/v1/',
});

export const getUserLocation = () => {
  const { GEOLOCATION_DB_TOKEN } = process.env;
  return {
    type: ACTION_TYPES.GET_LOCATION,
    payload: geoLocationDbAPI.get<UserLocation>(`json/${GEOLOCATION_DB_TOKEN}`),
  };
};

export const changeDistance = (distance: number): any => async dispatch =>
  await dispatch({
    type: SUCCESS(ACTION_TYPES.CHANGE_DISTANCE),
    payload: distance,
  });

export const findZipCode: any = (
  zipCode: string,
  country: string,
  locationSearchCallback: (wasFound: boolean) => void
) => async dispatch => {
  const { ZIPCODE_BASE_TOKEN } = process.env;
  const result = await zipcodebaseAPI.get(`search?apikey=${ZIPCODE_BASE_TOKEN}&codes=${zipCode}&country=${country}`);
  if (Object.keys(result.data.results).length === 0) {
    locationSearchCallback(false);
    return;
  } else {
    locationSearchCallback(true);
  }
  const firstItem: LocaleInformation = _.mapKeys(result.data.results[zipCode][0], (value, key) => _.camelCase(key));
  return await dispatch({
    type: SUCCESS(ACTION_TYPES.SEARCH_ZIPCODE),
    payload: {
      city: firstItem.city,
      countryCode: firstItem.countryCode,
      postal: firstItem.postalCode,
      state: firstItem.state ? firstItem.state : firstItem.province,
      latitude: firstItem.latitude,
      longitude: firstItem.longitude,
      distance: 25,
      conversionType: firstItem.countryCode === 'US' ? 'MI' : 'KM',
    },
  });
};
