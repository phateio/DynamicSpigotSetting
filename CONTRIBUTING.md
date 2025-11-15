# Contributing Guidelines

## Code Standards

### Language & Documentation

- Code comments: English preferred, but follow existing context
- Documentation: English preferred
- Principle: Self-documenting code over redundant comments

### Style Enforcement

- Ruby: Follow Rubocop conventions (`.rubocop.yml`)
- JavaScript: Follow ESLint conventions (`.eslintrc`)
- Python: Follow PEP 8 conventions
- Go: Follow gofmt conventions
- **Java: Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)**

### Commit Messages

- Follow [Conventional Commits](https://www.conventionalcommits.org/) specification

## Workflow

Run linters before committing:

```bash
# Ruby
rubocop -A

# JavaScript
eslint --fix .

# Python
black . && isort .

# Go
gofmt -w .

# Java
# Use google-java-format plugin in your IDE or:
# java -jar google-java-format.jar --replace src/**/*.java
```

- Ensure all tests pass
- Write clear, self-documenting code
- Commit with conventional format: `type(scope): description`
