export interface ItemReservationDto {
  wasReserved: boolean;
  errorMessage: string | null;
  reservedAmount: number;
  reservationId: number | null;
  reservationNumber: string | null;
}

export const defaultReservationValue: ItemReservationDto = {
  wasReserved: false,
  errorMessage: null,
  reservedAmount: 0,
  reservationId: null,
  reservationNumber: null,
};
