insert into docentenverantwoordelijkheden(docentId, verantwoordelijkheidId)
VALUES ((select id from docenten where voornaam ='testM'),
        (select id from verantwoordelijkheden where naam ='test'));
