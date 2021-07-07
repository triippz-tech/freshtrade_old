import React, { useEffect, useState } from 'react';
import { IRootState } from 'app/shared/reducers';
import { connect } from 'react-redux';
import AsyncSelect from 'react-select/async';
import { Link, RouteComponentProps } from 'react-router-dom';
import { getEntities as getLocations, loadLocations } from 'app/entities/location/location.reducer';
import { getEntities as getTradeEvents, loadEvents } from 'app/entities/trade-event/trade-event.reducer';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { createSellerEntity as createEntity, getEntity, reset, setBlob, updateEntityForSeller } from 'app/entities/item/item.reducer';
import { setFileData, translate, Translate } from 'react-jhipster';
import { convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { Button, Col, Label, Row } from 'reactstrap';
import { AvField, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IImage } from 'app/shared/model/image.model';
import { ILocation } from 'app/shared/model/location.model';
import { ITradeEvent } from 'app/shared/model/trade-event.model';

interface PostItemProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SellerItemUpdate = (props: PostItemProps) => {
  const [idsuser, setIdsuser] = useState([]);
  const [idscategories, setIdscategories] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);
  const [inputs, setInputs] = useState<Array<string>>(['input-0']);
  const [selectedLocation, setLocation] = useState<ILocation>(null);
  const [selectedEvent, setEvent] = useState<ITradeEvent>(null);
  const [{ isTradeErr, isLocationErr }, setErrs] = useState({ isTradeErr: false, isLocationErr: false });

  const { itemEntity, users, locations, tradeEvents, categories, loading, updating } = props;

  const { details } = itemEntity;

  const handleClose = () => {
    props.history.push('/seller/items');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getLocations();
    props.getTradeEvents();
    props.getCategories();
  }, []);

  useEffect(() => {
    setLocation(itemEntity.location);
    setEvent(itemEntity.tradeEvent);
  }, [itemEntity]);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    setErrs(prevState => ({ ...prevState, isLocationErr: selectedLocation === null }));
    setErrs(prevState => ({ ...prevState, isTradeErr: selectedEvent === null }));

    if (selectedEvent === null || selectedLocation === null) return;

    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.updatedDate = convertDateTimeToServer(values.updatedDate);

    const images: Array<IImage> = inputs
      .map((val, idx) => {
        const value = values[val] === null ? null : values[val];
        delete values[val];
        const img: IImage = {
          imageUrl: value,
        };
        return img;
      })
      .filter(function (el) {
        return el != null;
      });

    if (errors.length === 0) {
      const entity = {
        ...itemEntity,
        ...values,
        categories: mapIdList(values.categories),
        owner: props.currentUser,
        location: selectedLocation,
        tradeEvent: selectedEvent,
        images: images,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntityForSeller(entity);
      }
    }
  };

  const addAnotherImage = () => {
    const lastIn: string = inputs[inputs.length - 1];
    const num: number = Number.parseInt(lastIn[lastIn.length - 1], 2);
    const newNum = num + 1;

    setInputs([...inputs, `input-${newNum}`]);
  };

  const onEventSelect = val => setEvent(val === null ? null : val.event);

  const onLocationSelect = val => setLocation(val === null ? null : val.loc);

  const locationOptions = () =>
    props.locations.map(val => {
      return {
        label: val.shortName ? val.shortName : val.address,
        value: val.shortName ? val.shortName : val.address,
        loc: val,
      };
    });

  const getLocationOption = () =>
    selectedLocation === null || selectedLocation === undefined
      ? {}
      : {
          label: selectedLocation.shortName ? selectedLocation.shortName : selectedLocation.address,
          value: selectedLocation.shortName ? selectedLocation.shortName : selectedLocation.address,
          loc: selectedLocation,
        };

  const eventOptions = () =>
    props.tradeEvents.map(val => {
      return {
        label: val.eventName,
        value: val.eventName,
        event: val,
      };
    });

  const getEventOption = () =>
    selectedEvent === null || selectedEvent === undefined
      ? {}
      : {
          label: selectedEvent.eventName,
          value: selectedEvent.eventName,
          event: selectedEvent,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="12">
          <h2 id="freshtradeApp.item.home.createOrEditLabel" data-cy="ItemCreateUpdateHeading">
            {isNew ? (
              <Translate contentKey="freshtradeApp.item.seller.createLabel">Create New Item</Translate>
            ) : (
              <Translate contentKey="freshtradeApp.item.seller.editLabel">Edit Item</Translate>
            )}
          </h2>
        </Col>
      </Row>

      {loading ? (
        <p>Loading...</p>
      ) : (
        <AvForm model={isNew ? {} : itemEntity} onSubmit={saveEntity} className="w-100 form-outline">
          <Row className="justify-content-center">
            <Col md="6">
              <AvGroup>
                <Label id="priceLabel" for="item-price" class="form-label">
                  <Translate contentKey="freshtradeApp.item.price">Price</Translate>
                </Label>
                <AvField
                  id="item-price"
                  data-cy="price"
                  type="string"
                  className="form-control form-outline"
                  name="price"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="quantityLabel" for="item-quantity">
                  <Translate contentKey="freshtradeApp.item.quantity">Quantity</Translate>
                </Label>
                <AvField
                  id="item-quantity"
                  data-cy="quantity"
                  type="string"
                  className="form-control"
                  name="quantity"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    min: { value: 1, errorMessage: translate('entity.validation.min', { min: 1 }) },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="item-name">
                  <Translate contentKey="freshtradeApp.item.name">Name</Translate>
                </Label>
                <AvField
                  id="item-name"
                  data-cy="name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="detailsLabel" for="item-details">
                  <Translate contentKey="freshtradeApp.item.details">Details</Translate>
                </Label>
                <AvInput
                  id="item-details"
                  data-cy="details"
                  type="textarea"
                  name="details"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                {inputs.map((val, idx) => (
                  <div key={val}>
                    <Label id="detailsLabel" for={val}>
                      Image {idx + 1}
                    </Label>
                    <AvInput
                      id={val}
                      data-cy={val}
                      type="string"
                      name={val}
                      validate={{
                        pattern: {
                          value: '[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)',
                          errorMessage: 'Must provide a valid URL for image',
                        },
                      }}
                    />
                  </div>
                ))}
              </AvGroup>
              <AvGroup>
                <Button onClick={addAnotherImage} size="sm">
                  Add More Images
                </Button>
              </AvGroup>
            </Col>
            <Col md="6">
              {!isNew ? (
                <AvGroup hidden>
                  <Label for="item-id">
                    <Translate contentKey="freshtradeApp.item.id">Id</Translate>
                  </Label>
                  <AvInput id="item-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}

              <AvGroup>
                <Label id="itemConditionLabel" for="item-itemCondition">
                  <Translate contentKey="freshtradeApp.item.itemCondition">Item Condition</Translate>
                </Label>
                <AvInput
                  id="item-itemCondition"
                  data-cy="itemCondition"
                  type="select"
                  className="form-control"
                  name="itemCondition"
                  value={(!isNew && itemEntity.itemCondition) || 'NEW'}
                >
                  <option value="NEW">{translate('freshtradeApp.Condition.NEW')}</option>
                  <option value="OPEN_BOX">{translate('freshtradeApp.Condition.OPEN_BOX')}</option>
                  <option value="LIKE_NEW">{translate('freshtradeApp.Condition.LIKE_NEW')}</option>
                  <option value="USED">{translate('freshtradeApp.Condition.USED')}</option>
                  <option value="DAMAGED">{translate('freshtradeApp.Condition.DAMAGED')}</option>
                  <option value="FOR_PARTS">{translate('freshtradeApp.Condition.FOR_PARTS')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="item-location">
                  <Translate contentKey="freshtradeApp.item.location">Location</Translate>
                </Label>
                <AsyncSelect
                  isClearable={true}
                  loadOptions={props.loadLocations}
                  name="item-location"
                  defaultOptions={locationOptions()}
                  onChange={onLocationSelect}
                  value={getLocationOption()}
                  styles={{
                    menuPortal: base => ({ ...base, zIndex: 9999 }),
                    menu: provided => ({ ...provided, zIndex: '9999 !important' }),
                  }}
                />
                {isLocationErr && <div className="text-danger">Must Select a Location</div>}
              </AvGroup>
              <AvGroup>
                <Label for="item-tradeEvent">
                  <Translate contentKey="freshtradeApp.item.tradeEvent">Trade Event</Translate>
                </Label>
                <AsyncSelect
                  isClearable={true}
                  loadOptions={props.loadEvents}
                  name="item-tradeEvent"
                  defaultOptions={eventOptions()}
                  onChange={onEventSelect}
                  value={getEventOption()}
                  styles={{
                    menuPortal: base => ({ ...base, zIndex: 9999 }),
                    menu: provided => ({ ...provided, zIndex: '9999 !important' }),
                  }}
                />
                {isTradeErr && <div className="text-danger">Must Select an Event</div>}
                {/*<AvInput*/}
                {/*  id="item-tradeEvent"*/}
                {/*  data-cy="tradeEvent"*/}
                {/*  type="select"*/}
                {/*  className="form-control"*/}
                {/*  name="tradeEventId"*/}
                {/*  validate={{*/}
                {/*    required: {value: true, errorMessage: translate('entity.validation.required')},*/}
                {/*  }}*/}
                {/*>*/}
                {/*  <option value="" key="0"/>*/}
                {/*  {tradeEvents*/}
                {/*    ? tradeEvents.map(otherEntity => (*/}
                {/*      <option value={otherEntity.id} key={otherEntity.id}>*/}
                {/*        {otherEntity.eventName}*/}
                {/*      </option>*/}
                {/*    ))*/}
                {/*    : null}*/}
                {/*</AvInput>*/}
              </AvGroup>
              <AvGroup>
                <Label for="item-categories">
                  <Translate contentKey="freshtradeApp.item.categories">Categories</Translate>
                </Label>
                <AvInput
                  id="item-categories"
                  data-cy="categories"
                  type="select"
                  multiple
                  className="form-control"
                  name="categories"
                  value={!isNew && itemEntity.categories && itemEntity.categories.map(e => e.id)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                >
                  <option value="" key="0" />
                  {categories
                    ? categories.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
            </Col>
            <Row className="justify-content-center">
              <AvGroup check>
                <Label id="isActiveLabel">
                  <AvInput id="item-isActive" data-cy="isActive" type="checkbox" className="form-check-input" name="isActive" />
                  <Translate contentKey="freshtradeApp.item.isActive">Is Active</Translate>
                </Label>
              </AvGroup>
            </Row>
          </Row>
          <Row className="justify-content-center">
            <Button tag={Link} id="cancel-save" to="/seller/items" replace color="info">
              <FontAwesomeIcon icon="arrow-left" />
              &nbsp;
              <span className="d-none d-md-inline">
                <Translate contentKey="entity.action.back">Back</Translate>
              </span>
            </Button>
            &nbsp;
            <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
              <FontAwesomeIcon icon="save" />
              &nbsp;
              <Translate contentKey="entity.action.save">Save</Translate>
            </Button>
          </Row>
        </AvForm>
      )}
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  currentUser: storeState.authentication.account,
  users: storeState.userManagement.users,
  locations: storeState.location.entities,
  tradeEvents: storeState.tradeEvent.entities,
  categories: storeState.category.entities,
  itemEntity: storeState.item.entity,
  loading: storeState.item.loading,
  updating: storeState.item.updating,
  updateSuccess: storeState.item.updateSuccess,
});

const mapDispatchToProps = {
  getLocations,
  getTradeEvents,
  getCategories,
  getEntity,
  updateEntityForSeller,
  setBlob,
  createEntity,
  reset,
  loadEvents,
  loadLocations,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SellerItemUpdate);
