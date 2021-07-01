import React from 'react';
import { IRootState } from 'app/shared/reducers';
import { connect } from 'react-redux';

interface SellerMessagesProps {}

export const SellerMessages = (props: SellerMessagesProps) => {
  return <h1>SELLER Messages</h1>;
};

const mapStateToProps = ({ authentication }: IRootState) => ({
  isAuthenticated: authentication.isAuthenticated,
});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SellerMessages);
