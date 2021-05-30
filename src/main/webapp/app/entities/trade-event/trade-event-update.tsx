import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { getEntity, updateEntity, createEntity, reset } from './trade-event.reducer';
import { ITradeEvent } from 'app/shared/model/trade-event.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITradeEventUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TradeEventUpdate = (props: ITradeEventUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { tradeEventEntity, locations, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/trade-event');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getLocations();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);

    if (errors.length === 0) {
      const entity = {
        ...tradeEventEntity,
        ...values,
        location: locations.find(it => it.id.toString() === values.locationId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="freshtradeApp.tradeEvent.home.createOrEditLabel" data-cy="TradeEventCreateUpdateHeading">
            <Translate contentKey="freshtradeApp.tradeEvent.home.createOrEditLabel">Create or edit a TradeEvent</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : tradeEventEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="trade-event-id">
                    <Translate contentKey="freshtradeApp.tradeEvent.id">Id</Translate>
                  </Label>
                  <AvInput id="trade-event-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="eventNameLabel" for="trade-event-eventName">
                  <Translate contentKey="freshtradeApp.tradeEvent.eventName">Event Name</Translate>
                </Label>
                <AvField
                  id="trade-event-eventName"
                  data-cy="eventName"
                  type="text"
                  name="eventName"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="eventDescriptionLabel" for="trade-event-eventDescription">
                  <Translate contentKey="freshtradeApp.tradeEvent.eventDescription">Event Description</Translate>
                </Label>
                <AvField
                  id="trade-event-eventDescription"
                  data-cy="eventDescription"
                  type="text"
                  name="eventDescription"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="startDateLabel" for="trade-event-startDate">
                  <Translate contentKey="freshtradeApp.tradeEvent.startDate">Start Date</Translate>
                </Label>
                <AvInput
                  id="trade-event-startDate"
                  data-cy="startDate"
                  type="datetime-local"
                  className="form-control"
                  name="startDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.tradeEventEntity.startDate)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="endDateLabel" for="trade-event-endDate">
                  <Translate contentKey="freshtradeApp.tradeEvent.endDate">End Date</Translate>
                </Label>
                <AvInput
                  id="trade-event-endDate"
                  data-cy="endDate"
                  type="datetime-local"
                  className="form-control"
                  name="endDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.tradeEventEntity.endDate)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="isActiveLabel">
                  <AvInput id="trade-event-isActive" data-cy="isActive" type="checkbox" className="form-check-input" name="isActive" />
                  <Translate contentKey="freshtradeApp.tradeEvent.isActive">Is Active</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="trade-event-location">
                  <Translate contentKey="freshtradeApp.tradeEvent.location">Location</Translate>
                </Label>
                <AvInput id="trade-event-location" data-cy="location" type="select" className="form-control" name="locationId">
                  <option value="" key="0" />
                  {locations
                    ? locations.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/trade-event" replace color="info">
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
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  locations: storeState.location.entities,
  tradeEventEntity: storeState.tradeEvent.entity,
  loading: storeState.tradeEvent.loading,
  updating: storeState.tradeEvent.updating,
  updateSuccess: storeState.tradeEvent.updateSuccess,
});

const mapDispatchToProps = {
  getLocations,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TradeEventUpdate);
