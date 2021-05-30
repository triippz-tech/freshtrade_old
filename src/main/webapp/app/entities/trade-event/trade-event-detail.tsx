import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './trade-event.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITradeEventDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TradeEventDetail = (props: ITradeEventDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { tradeEventEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tradeEventDetailsHeading">
          <Translate contentKey="freshtradeApp.tradeEvent.detail.title">TradeEvent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="freshtradeApp.tradeEvent.id">Id</Translate>
            </span>
          </dt>
          <dd>{tradeEventEntity.id}</dd>
          <dt>
            <span id="eventName">
              <Translate contentKey="freshtradeApp.tradeEvent.eventName">Event Name</Translate>
            </span>
          </dt>
          <dd>{tradeEventEntity.eventName}</dd>
          <dt>
            <span id="eventDescription">
              <Translate contentKey="freshtradeApp.tradeEvent.eventDescription">Event Description</Translate>
            </span>
          </dt>
          <dd>{tradeEventEntity.eventDescription}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="freshtradeApp.tradeEvent.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {tradeEventEntity.startDate ? <TextFormat value={tradeEventEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="freshtradeApp.tradeEvent.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{tradeEventEntity.endDate ? <TextFormat value={tradeEventEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="freshtradeApp.tradeEvent.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{tradeEventEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="freshtradeApp.tradeEvent.location">Location</Translate>
          </dt>
          <dd>{tradeEventEntity.location ? tradeEventEntity.location.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/trade-event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trade-event/${tradeEventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ tradeEvent }: IRootState) => ({
  tradeEventEntity: tradeEvent.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TradeEventDetail);
