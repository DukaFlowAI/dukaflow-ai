const defaultApiBaseUrl = "http://localhost:8080/api/v1";

export const environment = {
  apiBaseUrl:
    import.meta.env.VITE_API_BASE_URL?.trim() || defaultApiBaseUrl,
};
