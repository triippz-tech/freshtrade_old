export interface ZipCodeResponse {
  query: Query;
  results: Results;
}
export interface Query {
  codes?: string[] | null;
  country?: null | string;
}
export interface Results {
  [key: string]: LocaleInformation;
}
export interface LocaleInformation {
  postalCode?: string;
  countryCode?: string;
  latitude?: string;
  longitude?: string;
  city?: string;
  state?: string;
  stateCode?: string;
  province?: string | null;
  provinceCode?: string | null;
}
