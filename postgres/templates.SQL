INSERT INTO
  email_templates (id, name, subject, content)
VALUES
  (
    1,
    'password_forgotten',
    'Password forgotten',
    '<html lang="fr">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Password forgotten</title>
  </head>
  <body
    style="
      margin: 20px;
      padding: 0;
      font-family: Arial, sans-serif;
      background-color: none;
    "
  >
    <!-- Body -->
    <div
      style="
        width: 100%;
        max-width: 600px;
        margin: auto;
        background-color: #ffffff;
        border-radius: 15px;
        overflow: hidden;
        border: 1px solid #d3d3d3;
        text-align: center;
      "
    >
      <!-- Header -->
      <header style="background-color: #002d5e; padding: 1px">
        <img
          src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Rdf_logo.svg/768px-Rdf_logo.svg.png"
          alt="RDF logo"
          style="
            width: 100px;
            max-width: 100%;
            height: auto;
            display: block;
            margin: 5px auto;
          "
        />
      </header>

      <!-- Content -->
      <div
        style="padding: 20px; color: #555; font-size: 16px; line-height: 1.6"
      >
        <h1 style="color: #3f4d75; margin-bottom: 20px">
          PASSWORD FORGOTTEN ?
        </h1>

        <p>No worries !</p>

        <img
          src="https://d1oco4z2z1fhwp.cloudfront.net/templates/default/3856/GIF_password.gif"
          alt="Wrong Password Animation"
          style="
            max-width: 100%;
            height: auto;
            display: block;
            margin: 20px auto;
            border-radius: 10px;
          "
        />

        <h1 style="color: #3f4d75">Here is your verification code</h1>

        <span
          style="
            color: #3f4d75;
            font-size: 30px;
            font-weight: bold;
            letter-spacing: 5px;
          "
          th:text="${code}"
        >
          123456
        </span>

      <!-- Divider -->
      <hr style="border: none; border-top: 1px solid #d3d3d3; margin: 0 20px" />

      <!-- Footer -->
      <footer style="padding: 0 20px; font-size: 14px; color: #888">
        <p>
          If you have any questions, please do not hesitate to contact us at
          <a
            href="mailto:noreplyannotation@gmail.com"
            style="color: #002d5e; text-decoration: none"
            >noreplyannotation@gmail.com</a
          >.
        </p>
        <p style="font-size: 12px; margin-top: 10px">
          &copy; 2025 Yasmine Boudiaf. All rights reserved.
        </p>
      </footer>
    </div>
  </body>
</html>'
  );

INSERT INTO
  email_templates (id, name, subject, content)
VALUES
  (
    2,
    'verification_code_expired',
    'Verification code expired',
    '<html lang="fr">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Verification code expired</title>
  </head>
  <body
    style="
      margin: 20px;
      padding: 0;
      font-family: Arial, sans-serif;
      background-color: none;
    "
  >
    <!-- Body -->
    <div
      style="
        width: 100%;
        max-width: 600px;
        margin: auto;
        background-color: #ffffff;
        border-radius: 15px;
        overflow: hidden;
        border: 1px solid #d3d3d3;
        text-align: center;
      "
    >
      <!-- Header -->
      <header style="background-color: #002d5e; padding: 1px">
        <img
          src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Rdf_logo.svg/768px-Rdf_logo.svg.png"
          alt="RDF logo"
          style="
            width: 100px;
            max-width: 100%;
            height: auto;
            display: block;
            margin: 5px auto;
          "
        />
      </header>

      <!-- Content -->
      <div
        style="padding: 20px; color: #555; font-size: 16px; line-height: 1.6"
      >
        <h1 style="color: #3f4d75; margin-bottom: 20px">
          OOPS, YOUR VERIFICATION CODE HAS EXPIRED
        </h1>

        <p>Pas de soucis !</p>

        <img
          src="https://d1oco4z2z1fhwp.cloudfront.net/templates/default/3856/GIF_password.gif"
          alt="Wrong Password Animation"
          style="
            max-width: 100%;
            height: auto;
            display: block;
            margin: 20px auto;
            border-radius: 10px;
          "
        />

        <h1 style="color: #3f4d75">Here is a new verification code</h1>

        <span
          style="
            color: #3f4d75;
            font-size: 30px;
            font-weight: bold;
            letter-spacing: 5px;
          "
          th:text="${code}"
        >
          123456
        </span>

        <p style="font-size: 12px; color: #888">
          This code will expire in 1 hour.
        </p>
      </div>

      <!-- Divider -->
      <hr style="border: none; border-top: 1px solid #d3d3d3; margin: 0 20px" />

      <!-- Footer -->
      <footer style="padding: 0 20px; font-size: 14px; color: #888">
        <p>
          If you have any questions, please do not hesitate to contact us at
          <a
            href="mailto:noreplyannotation@gmail.com"
            style="color: #002d5e; text-decoration: none"
            >noreplyannotation@gmail.com</a
          >.
        </p>
        <p style="font-size: 12px; margin-top: 10px">
          &copy; 2025 Yasmine Boudiaf. All rights reserved.
        </p>
      </footer>
    </div>
  </body>
</html>'
  );

INSERT INTO
  email_templates (id, name, subject, content)
VALUES
  (
    4,
    'new_account',
    'New account',
    '<html lang="fr">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>New account</title>
  </head>
  <body
    style="
      margin: 20px;
      padding: 0;
      font-family: Arial, sans-serif;
      background-color: none;
    "
  >
    <!-- Body -->
    <div
      style="
        width: 100%;
        max-width: 600px;
        margin: auto;
        background-color: #ffffff;
        border-radius: 15px;
        overflow: hidden;
        border: 1px solid #d3d3d3;
        text-align: center;
      "
    >
      <!-- Header -->
      <header style="background-color: #002d5e; padding: 1px">
        <img
          src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Rdf_logo.svg/768px-Rdf_logo.svg.png"
          alt="RDF logo"
          style="
            width: 100px;
            max-width: 100%;
            height: auto;
            display: block;
            margin: 5px auto;
          "
        />
      </header>

      <!-- Content -->
      <div
        style="padding: 20px; color: #555; font-size: 16px; line-height: 1.6"
      >
        <h1 style="color: #3f4d75; margin-bottom: 20px">
          WELCOME TO OUR PLATFORM
        </h1>

        <p>
          Welcome
          <strong>
            <span th:text="${firstName}">Jake</span>
            <span th:text="${lastName}">Huswvfps</span>
          </strong>
        </p>

        <p>
          Thank you for joining us. Below is the link to verify your account.
        </p>

        <!-- Link -->
        <div style="margin: 30px 0">
          <a
            th:href="${apiBaseUrl + &#39;/auth/validate?email=&#39; + email + &#39;&code=&#39; + code}"
            style="
              background-color: #002d5e;
              color: #ffffff;
              padding: 12px 30px;
              border-radius: 5px;
              text-decoration: none;
              font-weight: bold;
            "
            >NEXT</a
          >
        </div>

        <p style="font-size: 12px; color: #888">
          This link will expire in 24 hours.
        </p>
      </div>

      <!-- Divider -->
      <hr style="border: none; border-top: 1px solid #d3d3d3; margin: 0 20px" />

      <!-- Footer -->
      <footer style="padding: 0 20px; font-size: 14px; color: #888">
        <p>
          If you have any questions, please do not hesitate to contact us at
          <a
            href="mailto:noreplyannotation@gmail.com"
            style="color: #002d5e; text-decoration: none"
            >noreplyannotation@gmail.com</a
          >.
        </p>
        <p style="font-size: 12px; margin-top: 10px">
          &copy; 2025 Yasmine Boudiaf. All rights reserved.
        </p>
      </footer>
    </div>
  </body>
</html>'
  );

INSERT INTO
  email_templates (id, name, subject, content)
VALUES
  (
    3,
    'invitation_to_join_campaign',
    'Join campaign',
    '<html lang="fr">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Invitation to take part in the campaign</title>
  </head>
  <body
    style="
      margin: 20px;
      padding: 0;
      font-family: Arial, sans-serif;
      background-color: none;
    "
  >
    <!-- Body -->
    <div
      style="
        width: 100%;
        max-width: 600px;
        margin: auto;
        background-color: #ffffff;
        border-radius: 15px;
        overflow: hidden;
        border: 1px solid #d3d3d3;
        text-align: center;
      "
    >
      <!-- Header -->
      <header style="background-color: #002d5e; padding: 1px">
        <img
          src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Rdf_logo.svg/768px-Rdf_logo.svg.png"
          alt="RDF logo"
          style="
            width: 100px;
            max-width: 100%;
            height: auto;
            display: block;
            margin: 5px auto;
          "
        />
      </header>

      <!-- Content -->
      <div
        style="padding: 20px; color: #555; font-size: 16px; line-height: 1.6"
      >
        <h1 style="color: #3f4d75; margin-bottom: 20px">
          YOU HAVE AN INVITATION TO TAKE PART IN THE CAMPAIGN
        </h1>

        <p>
          <strong>
            <span th:text="${campaignName}">Campaign Name</span>
          </strong>
        </p>

        <p>Below is the link to your invitation.</p>

        <!-- Link -->
        <div style="margin: 30px 0">
          <a
            th:href="${apiBaseUrl + &#39;/campaign/accept?participantEmail=&#39; + email + &#39;&campaignName=&#39; + campaignName}"
            style="
              background-color: #002d5e;
              color: #ffffff;
              padding: 12px 30px;
              border-radius: 5px;
              text-decoration: none;
              font-weight: bold;
            "
            >NEXT</a
          >
        </div>
      </div>

      <!-- Divider -->
      <hr style="border: none; border-top: 1px solid #d3d3d3; margin: 0 20px" />

      <!-- Footer -->
      <footer style="padding: 0 20px; font-size: 14px; color: #888">
        <p>
          If you have any questions, please do not hesitate to contact us at
          <a
            href="mailto:noreplyannotation@gmail.com"
            style="color: #002d5e; text-decoration: none"
            >noreplyannotation@gmail.com</a
          >.
        </p>
        <p style="font-size: 12px; margin-top: 10px">
          &copy; 2025 Yasmine Boudiaf. All rights reserved.
        </p>
      </footer>
    </div>
  </body>
</html>'
  );