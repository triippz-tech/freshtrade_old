type ConversionType = 'KM' | 'MI';

export interface UserLocation {
  countryCode?: string;
  countryName?: string;
  city?: string;
  postal?: string;
  latitude?: string;
  longitude?: string;
  IP?: string;
  state?: string;
  distance?: number | null;
  conversionType?: ConversionType | null;
}

export const defaultValue: Readonly<UserLocation> = {
  city: 'Lancaster',
  countryCode: 'US',
  countryName: 'United States',
  postal: '17552',
  state: 'PA',
  latitude: '40.1170',
  longitude: '76.5141',
  distance: 25,
  conversionType: 'MI',
};
