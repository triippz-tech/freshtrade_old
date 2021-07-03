import React, { useEffect, useState } from 'react';
import { IRootState } from 'app/shared/reducers';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { getEntities as getTradeEvents } from 'app/entities/trade-event/trade-event.reducer';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { createEntity, reset, setBlob } from 'app/entities/item/item.reducer';
import { setFileData, translate, Translate } from 'react-jhipster';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ItemStep1 } from 'app/components/builder/item-steps';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

interface PostItemProps extends StateProps, DispatchProps, RouteComponentProps {}

export const SellerItemNew = (props: PostItemProps) => {
  const { userList, locations, tradeEvents, categoryList, loading, updating } = props;
  const [step, setStep] = useState(1);

  const handleClose = () => {
    props.history.push('/item');
  };

  useEffect(() => {
    props.getLocations();
    props.getTradeEvents();
    props.getCategories();
  }, []);

  const onBlobChange = (isAnImage, namex) => event => {
    setFileData(event, (contentType, data) => props.setBlob(namex, data, contentType), isAnImage);
  };

  const clearBlob = namex => () => {
    props.setBlob(namex, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.updatedDate = convertDateTimeToServer(values.updatedDate);

    if (errors.length === 0) {
      const entity = {
        ...values,
        users: mapIdList(values.users),
        categories: mapIdList(values.categories),
        owner: props.currentUser,
        location: locations.find(it => it.id.toString() === values.locationId.toString()),
        tradeEvent: tradeEvents.find(it => it.id.toString() === values.tradeEventId.toString()),
      };

      props.createEntity(entity);
    }
  };

  const renderStep = () => {
    switch (step) {
      case 1:
        return <ItemStep1 />;
      default:
        return <ItemStep1 />;
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="12">
          <h2 id="freshtradeApp.item.home.createOrEditLabel" data-cy="ItemCreateUpdateHeading">
            <Translate contentKey="freshtradeApp.item.seller.createLabel">Create New Item</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={{}} onSubmit={saveEntity}>
              {renderStep()}
              {step > 1 && (
                <Button color="secondary" id="back-step" data-cy="backStepButton" disabled={updating} onClick={() => setStep(step - 1)}>
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <Translate contentKey="freshtradeApp.item.create.back">Back</Translate>
                </Button>
              )}
              &nbsp;
              <Button color="primary" id="next-step" data-cy="nextStepButton" disabled={updating} onClick={() => setStep(step + 1)}>
                <Translate contentKey="freshtradeApp.item.create.next">Next</Translate>
                &nbsp;
                <FontAwesomeIcon icon="arrow-right" />
              </Button>
            </AvForm>

            // <AvForm model={{}} onSubmit={saveEntity}>
            //   <AvGroup>
            //     <Label id="priceLabel" for="item-price">
            //       <Translate contentKey="freshtradeApp.item.price">Price</Translate>
            //     </Label>
            //     <AvField
            //       id="item-price"
            //       data-cy="price"
            //       type="string"
            //       className="form-control"
            //       name="price"
            //       validate={{
            //         required: { value: true, errorMessage: translate('entity.validation.required') },
            //         number: { value: true, errorMessage: translate('entity.validation.number') },
            //       }}
            //     />
            //   </AvGroup>
            //   <AvGroup>
            //     <Label id="quantityLabel" for="item-quantity">
            //       <Translate contentKey="freshtradeApp.item.quantity">Quantity</Translate>
            //     </Label>
            //     <AvField
            //       id="item-quantity"
            //       data-cy="quantity"
            //       type="string"
            //       className="form-control"
            //       name="quantity"
            //       validate={{
            //         required: { value: true, errorMessage: translate('entity.validation.required') },
            //         min: { value: 1, errorMessage: translate('entity.validation.min', { min: 1 }) },
            //         number: { value: true, errorMessage: translate('entity.validation.number') },
            //       }}
            //     />
            //   </AvGroup>
            //   <AvGroup>
            //     <Label id="nameLabel" for="item-name">
            //       <Translate contentKey="freshtradeApp.item.name">Name</Translate>
            //     </Label>
            //     <AvField
            //       id="item-name"
            //       data-cy="name"
            //       type="text"
            //       name="name"
            //       validate={{
            //         required: { value: true, errorMessage: translate('entity.validation.required') },
            //       }}
            //     />
            //   </AvGroup>
            //   <AvGroup>
            //     <Label id="detailsLabel" for="item-details">
            //       <Translate contentKey="freshtradeApp.item.details">Details</Translate>
            //     </Label>
            //     <AvInput
            //       id="item-details"
            //       data-cy="details"
            //       type="textarea"
            //       name="details"
            //       validate={{
            //         required: { value: true, errorMessage: translate('entity.validation.required') },
            //       }}
            //     />
            //   </AvGroup>
            //   <AvGroup>
            //     <Label id="itemConditionLabel" for="item-itemCondition">
            //       <Translate contentKey="freshtradeApp.item.itemCondition">Item Condition</Translate>
            //     </Label>
            //     <AvInput
            //       id="item-itemCondition"
            //       data-cy="itemCondition"
            //       type="select"
            //       className="form-control"
            //       name="itemCondition"
            //     >
            //       <option value="NEW">{translate('freshtradeApp.Condition.NEW')}</option>
            //       <option value="OPEN_BOX">{translate('freshtradeApp.Condition.OPEN_BOX')}</option>
            //       <option value="LIKE_NEW">{translate('freshtradeApp.Condition.LIKE_NEW')}</option>
            //       <option value="USED">{translate('freshtradeApp.Condition.USED')}</option>
            //       <option value="DAMAGED">{translate('freshtradeApp.Condition.DAMAGED')}</option>
            //       <option value="FOR_PARTS">{translate('freshtradeApp.Condition.FOR_PARTS')}</option>
            //     </AvInput>
            //   </AvGroup>
            //   <AvGroup check>
            //     <Label id="isActiveLabel">
            //       <AvInput id="item-isActive" data-cy="isActive" type="checkbox" className="form-check-input" name="isActive" />
            //       <Translate contentKey="freshtradeApp.item.isActive">Is Active</Translate>
            //     </Label>
            //   </AvGroup>
            //   <AvGroup>
            //     <Label for="item-location">
            //       <Translate contentKey="freshtradeApp.item.location">Location</Translate>
            //     </Label>
            //     <AvInput id="item-location" data-cy="location" type="select" className="form-control" name="locationId">
            //       <option value="" key="0" />
            //       {locations
            //         ? locations.map(otherEntity => (
            //           <option value={otherEntity.id} key={otherEntity.id}>
            //             {otherEntity.shortName}
            //           </option>
            //         ))
            //         : null}
            //     </AvInput>
            //   </AvGroup>
            //   <AvGroup>
            //     <Label for="item-tradeEvent">
            //       <Translate contentKey="freshtradeApp.item.tradeEvent">Trade Event</Translate>
            //     </Label>
            //     <AvInput id="item-tradeEvent" data-cy="tradeEvent" type="select" className="form-control" name="tradeEventId">
            //       <option value="" key="0" />
            //       {tradeEvents
            //         ? tradeEvents.map(otherEntity => (
            //           <option value={otherEntity.id} key={otherEntity.id}>
            //             {otherEntity.eventName}
            //           </option>
            //         ))
            //         : null}
            //     </AvInput>
            //   </AvGroup>
            //   <AvGroup>
            //     <Label for="item-categories">
            //       <Translate contentKey="freshtradeApp.item.categories">Categories</Translate>
            //     </Label>
            //     <AvInput
            //       id="item-categories"
            //       data-cy="categories"
            //       type="select"
            //       multiple
            //       className="form-control"
            //       name="categories"
            //     >
            //       <option value="" key="0" />
            //       {categories
            //         ? categories.map(otherEntity => (
            //           <option value={otherEntity.id} key={otherEntity.id}>
            //             {otherEntity.name}
            //           </option>
            //         ))
            //         : null}
            //     </AvInput>
            //   </AvGroup>
            //   <Button tag={Link} id="cancel-save" to="/item" replace color="info">
            //     <FontAwesomeIcon icon="arrow-left" />
            //     &nbsp;
            //     <span className="d-none d-md-inline">
            //       <Translate contentKey="entity.action.back">Back</Translate>
            //     </span>
            //   </Button>
            //   &nbsp;
            //   <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
            //     <FontAwesomeIcon icon="save" />
            //     &nbsp;
            //     <Translate contentKey="entity.action.save">Save</Translate>
            //   </Button>
            // </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  currentUser: storeState.authentication.account,
  userList: storeState.userManagement.users,
  locations: storeState.location.entities,
  tradeEvents: storeState.tradeEvent.entities,
  categoryList: storeState.category.entities,
  itemEntity: storeState.item.entity,
  loading: storeState.item.loading,
  updating: storeState.item.updating,
  updateSuccess: storeState.item.updateSuccess,
});

const mapDispatchToProps = {
  getLocations,
  getTradeEvents,
  getCategories,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SellerItemNew);
