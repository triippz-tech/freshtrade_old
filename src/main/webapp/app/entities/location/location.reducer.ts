import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction,
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILocation, defaultValue } from 'app/shared/model/location.model';

export const ACTION_TYPES = {
  FETCH_LOCATION_LIST: 'location/FETCH_LOCATION_LIST',
  FETCH_LOCATION: 'location/FETCH_LOCATION',
  CREATE_LOCATION: 'location/CREATE_LOCATION',
  UPDATE_LOCATION: 'location/UPDATE_LOCATION',
  PARTIAL_UPDATE_LOCATION: 'location/PARTIAL_UPDATE_LOCATION',
  DELETE_LOCATION: 'location/DELETE_LOCATION',
  RESET: 'location/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILocation>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type LocationState = Readonly<typeof initialState>;

// Reducer

export default (state: LocationState = initialState, action): LocationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_LOCATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LOCATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_LOCATION):
    case REQUEST(ACTION_TYPES.UPDATE_LOCATION):
    case REQUEST(ACTION_TYPES.DELETE_LOCATION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_LOCATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_LOCATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LOCATION):
    case FAILURE(ACTION_TYPES.CREATE_LOCATION):
    case FAILURE(ACTION_TYPES.UPDATE_LOCATION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_LOCATION):
    case FAILURE(ACTION_TYPES.DELETE_LOCATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOCATION_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_LOCATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_LOCATION):
    case SUCCESS(ACTION_TYPES.UPDATE_LOCATION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_LOCATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_LOCATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/locations';

// Actions

export const getEntities: ICrudGetAllAction<ILocation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_LOCATION_LIST,
    payload: axios.get<ILocation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ILocation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LOCATION,
    payload: axios.get<ILocation>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ILocation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LOCATION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<ILocation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LOCATION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ILocation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_LOCATION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILocation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LOCATION,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
