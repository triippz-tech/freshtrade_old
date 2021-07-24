import React, { useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { IRootState } from 'app/shared/reducers';
import { redeemReservation } from 'app/entities/reservation/reservation.reducer';
import { connect } from 'react-redux';
import { Container, Row } from 'reactstrap';
import Reward from 'react-rewards';

export interface RedeemReservationProps extends StateProps, DispatchProps, RouteComponentProps<{ reservationNumber: string }> {}

const RedeemReservation = (props: RedeemReservationProps) => {
  const [wasSuccess, setWasSuccess] = useState<boolean>(null);

  let reward = null;

  useEffect(() => {
    props.redeemReservation(props.match.params.reservationNumber);
  }, []);

  const handleClose = () => {
    props.history.push('/account/reservations');
  };

  useEffect(() => {
    if (wasSuccess) {
      reward.rewardMe();
    } else handleClose();
  }, [wasSuccess]);

  useEffect(() => {
    console.log(`RESERVATION WAS: ${props.updateSuccess}`);
    setWasSuccess(props.updateSuccess);
  }, [props.updateSuccess]);

  return (
    <Container
      style={{
        position: 'absolute',
        height: '50px',
        top: '50%',
        marginTop: '-25px',
        left: '50%',
        marginLeft: '-50px' /* margin is -0.5 * dimension */,
      }}
    >
      <Reward
        ref={ref => {
          reward = ref;
        }}
        type="confetti"
        config={{
          lifetime: 200,
          spread: 100,
          elementCount: 200,
        }}
      >
        {props.reservation.id && (
          <>
            <Row>
              <h4>Successfully Redeemed: {props.reservation.reservationNumber}!</h4>
            </Row>
            <Row>
              <img
                loading="eager"
                src={
                  props.reservation.item.images && props.reservation.item.images.length > 0
                    ? props.reservation.item.images[0].imageUrl
                    : 'content/images/logo-gray.png'
                }
                alt="item image"
              />
            </Row>
          </>
        )}
      </Reward>
    </Container>
  );
};

const mapStateToProps = ({ reservation }: IRootState) => ({
  updateSuccess: reservation.updateSuccess,
  reservation: reservation.entity,
});

const mapDispatchToProps = { redeemReservation };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RedeemReservation);
