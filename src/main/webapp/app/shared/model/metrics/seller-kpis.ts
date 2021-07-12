export interface SellerKpis {
  totalRevenue: number;
  monthlyRevenue: number;
  pending7DReservations: number;
  totalActiveReservations: number;
  uniqueBuyers: number;
  returningBuyers: number;
  totalCancellations: number;
  totalCompletedReservations: number;
}

export const defaultSellerKPIs: SellerKpis = {
  totalRevenue: 0,
  monthlyRevenue: 0,
  pending7DReservations: 0,
  totalActiveReservations: 0,
  uniqueBuyers: 0,
  returningBuyers: 0,
  totalCancellations: 0,
  totalCompletedReservations: 0,
};
