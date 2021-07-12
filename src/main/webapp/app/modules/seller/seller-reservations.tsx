import React, { useEffect, useState } from 'react';
import { IRootState } from 'app/shared/reducers';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Col, Container, Row } from 'reactstrap';
import { getSortState, Translate } from 'react-jhipster';
import SellerReservationCard from 'app/components/seller-reservation-card';
import { cancelReservation, getSellerReservations, reset } from 'app/entities/reservation/reservation.reducer';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { RouteComponentProps } from 'react-router-dom';
import { IReservation } from 'app/shared/model/reservation.model';
import MessageUserDialog from 'app/components/message-user-dialog';
import CancelReservationDialog from 'app/components/cancel-reservation-dialog';
import ExchangeReservationDialog from 'app/components/exchange-reservation-dialog';
import { toast } from 'react-toastify';

interface SellerReservationsProps extends StateProps, DispatchProps, RouteComponentProps {}

export const SellerReservations = (props: SellerReservationsProps) => {
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );
  const [sorting, setSorting] = useState(false);
  const [cancelDialogOpen, setCancelDialogOpen] = useState(false);
  const [exchangeDialogOpen, setExchangeDialogOpen] = useState(false);
  const [messageDialogOpen, setMessageDialogOpen] = useState(false);
  const [selectedReservation, setSelectedReservation] = useState<IReservation>(null);
  const [messageValue, setMessageValue] = useState<string>('');
  const [cancelMessage, setCancelMessage] = useState<string>('');

  useEffect(() => {
    if (!cancelDialogOpen) setSelectedReservation(null);
  }, [cancelDialogOpen]);

  useEffect(() => {
    if (!exchangeDialogOpen) setSelectedReservation(null);
  }, [exchangeDialogOpen]);

  useEffect(() => {
    if (!messageDialogOpen) setSelectedReservation(null);
    else setMessageValue('');
  }, [messageDialogOpen]);

  const getAllEntities = () => {
    props.getSellerReservations(
      paginationState.activePage - 1,
      paginationState.itemsPerPage,
      `${paginationState.sort},${paginationState.order}`
    );
  };

  const resetAll = () => {
    props.reset();
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    props.getSellerReservations();
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      resetAll();
    }
  }, [props.updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    props.reset();
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const sendBuyerMessage = () => {
    console.log(`Sending Message to User: ${messageValue}`);
    toast.success('Message Sent!');
    setMessageDialogOpen(false);
  };

  const cancelReservations = () => {
    props.cancelReservation(
      {
        id: selectedReservation.id,
        cancellationNote: cancelMessage,
      },
      () => {
        setCancelMessage('');
        setCancelDialogOpen(false);
        getAllEntities();
      }
    );
  };

  return (
    <div>
      <MessageUserDialog
        value={messageValue}
        onChange={val => setMessageValue(val)}
        onSubmit={sendBuyerMessage}
        isOpen={messageDialogOpen}
        toggle={() => setMessageDialogOpen(!messageDialogOpen)}
        userName={selectedReservation && selectedReservation.buyer ? selectedReservation.buyer.login : ''}
      />

      {selectedReservation && (
        <CancelReservationDialog
          value={cancelMessage}
          onChange={val => setCancelMessage(val)}
          onSubmit={cancelReservations}
          isOpen={cancelDialogOpen}
          toggle={() => setCancelDialogOpen(!cancelDialogOpen)}
          reservation={selectedReservation}
        />
      )}

      {selectedReservation && (
        <ExchangeReservationDialog
          isOpen={exchangeDialogOpen}
          toggle={() => setExchangeDialogOpen(!exchangeDialogOpen)}
          reservation={selectedReservation}
        />
      )}

      <Row className="justify-content-center">
        <Container>
          <h2 id="freshtradeApp.item.home.createOrEditLabel" data-cy="ItemCreateUpdateHeading">
            <Translate contentKey="freshtradeApp.reservation.sellers.title">Create New Item</Translate>
          </h2>
        </Container>
      </Row>

      <Row>
        <Container>
          <InfiniteScroll
            pageStart={paginationState.activePage}
            loadMore={handleLoadMore}
            hasMore={paginationState.activePage - 1 < props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            {props.reservations.map((reservation, idx) => (
              <SellerReservationCard
                reservation={reservation}
                key={reservation.id}
                onCancelReservation={reservation1 => {
                  setSelectedReservation(reservation1);
                  setCancelDialogOpen(true);
                }}
                onMessageBuyer={reservation1 => {
                  setSelectedReservation(reservation1);
                  setMessageDialogOpen(true);
                }}
                onExchangeItem={reservation1 => {
                  setSelectedReservation(reservation1);
                  setExchangeDialogOpen(true);
                }}
              />
            ))}
          </InfiniteScroll>
        </Container>
      </Row>
    </div>
  );
};

const mapStateToProps = ({ authentication, reservation }: IRootState) => ({
  isAuthenticated: authentication.isAuthenticated,
  reservations: reservation.entities,
  loading: reservation.loading,
  totalItems: reservation.totalItems,
  links: reservation.links,
  entity: reservation.entity,
  updateSuccess: reservation.updateSuccess,
});

const mapDispatchToProps = {
  getSellerReservations,
  reset,
  cancelReservation,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SellerReservations);
