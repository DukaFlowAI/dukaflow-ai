import {
  Box,
  Button,
  Container,
  Paper,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { Link } from "react-router";

export function LoginPage() {
  return (
    <Container maxWidth="sm">
      <Box
        sx={{
          minHeight: "100vh",
          display: "flex",
          alignItems: "center",
        }}
      >
        <Paper elevation={3} sx={{ width: "100%", p: 4 }}>
          <Stack spacing={3}>
            <Box>
              <Typography variant="h4" component="h1">
                DukaFlow AI
              </Typography>

              <Typography color="text.secondary">
                Sign in to manage your retail business.
              </Typography>
            </Box>

            <TextField
              label="Email or username"
              type="text"
              fullWidth
              required
            />

            <TextField
              label="Password"
              type="password"
              fullWidth
              required
            />

            <Button variant="contained" size="large">
              Sign in
            </Button>

            <Button component={Link} to="/dashboard">
              View setup dashboard
            </Button>
          </Stack>
        </Paper>
      </Box>
    </Container>
  );
}
