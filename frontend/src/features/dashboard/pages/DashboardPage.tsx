import {
  AppBar,
  Box,
  Button,
  Container,
  Paper,
  Stack,
  Toolbar,
  Typography,
} from "@mui/material";
import { Link } from "react-router";

export function DashboardPage() {
  return (
    <Box sx={{ minHeight: "100vh", bgcolor: "background.default" }}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            DukaFlow AI
          </Typography>

          <Button color="inherit" component={Link} to="/login">
            Logout
          </Button>
        </Toolbar>
      </AppBar>

      <Container sx={{ py: 4 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Dashboard
            </Typography>

            <Typography color="text.secondary">
              React and TypeScript frontend setup completed successfully.
            </Typography>
          </Box>

          <Paper sx={{ p: 3 }}>
            <Typography variant="h6">
              Frontend status
            </Typography>

            <Typography sx={{ mt: 1 }}>
              Material UI, React Router and the initial project structure are
              configured.
            </Typography>
          </Paper>
        </Stack>
      </Container>
    </Box>
  );
}
