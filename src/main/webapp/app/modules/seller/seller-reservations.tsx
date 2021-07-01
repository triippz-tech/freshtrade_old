import React from 'react';
import { IRootState } from 'app/shared/reducers';
import { connect } from 'react-redux';

interface SellerReservationsProps {}

export const SellerReservations = (props: SellerReservationsProps) => {
  return <h1>SELLERS</h1>;
};

const mapStateToProps = ({ authentication }: IRootState) => ({
  isAuthenticated: authentication.isAuthenticated,
});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SellerReservations);
