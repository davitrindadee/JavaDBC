# SoulCode — Java + Oracle

Projeto Maven/Spring Boot com uma única raiz de aplicação.

## Abrir no IntelliJ

1. Abra a pasta `SoulCode` (a que contém `pom.xml`).
2. Importe/recarregue o Maven e configure um JDK 17 ou superior.
3. Abra `src/main/java/br/com/fiap/soulcode/connection/ConnectionFactory.java`.
4. Substitua `SEU_RM` e `SUA_SENHA` pelas suas credenciais locais, **sem enviar o arquivo editado ao GitHub**.
5. Execute `br.com.fiap.soulcode.test.TesteConexao` para validar a conexão; depois execute `TesteSistema`.

A URL JDBC já está no `ConnectionFactory.java`: `jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl` (SID `orcl`).

## Avisos

- O ZIP não inclui `target/`, `.idea/`, `.iml`, nem arquivos compilados. O Maven gerará `target/` localmente ao compilar; ele é ignorado pelo Git.
- O `.gitignore` evita arquivos gerados, mas **não protege senhas digitadas no `ConnectionFactory.java`**. Antes de qualquer commit, restaure os placeholders ou migre as credenciais para variáveis de ambiente.
- O script de sequências Oracle, caso necessário, deve ser aplicado separadamente ao DDL, conforme a configuração do seu banco.
- O teste do sistema pode cadastrar dados; executar repetidas vezes com o mesmo e-mail pode causar ORA-00001.
