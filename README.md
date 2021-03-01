# java-proto-validator

## Usage

```
  public ProtobufValidator protoValidator() {
    return ProtobufValidator.createBuilder()
        .registerValidator(
            MessageValidator.createBuilder(User.getDescriptor())
                .addFieldConstraint("firstName", FieldConstraints.IS_SET)
                .addFieldConstraint("firstName", FieldConstraints.length(1, 50))
                .addFieldConstraint("lastName", FieldConstraints.IS_SET)
                .addFieldConstraint("lastName", FieldConstraints.length(1, 50))
                .addFieldConstraint("address", FieldConstraints.IS_SET)
                .addFieldConstraint("address", FieldConstraints.VALID)
                .addMessageConstraint(MessageConstraints.oneOfIsSet("phoneNumber"))
                .build())
        .registerValidator(
            MessageValidator.createBuilder(Address.getDescriptor())
                .addFieldConstraint("number", FieldConstraints.IS_SET)
                .addFieldConstraint("number", FieldConstraints.POSITIVE)
                .addFieldConstraint("name", FieldConstraints.IS_SET)
                .addFieldConstraint("name", FieldConstraints.length(1, 100))
                .addFieldConstraint("unit", FieldConstraints.POSITIVE)
                .build())
        .build();
  }
```
  
## Constraints

There are Field level constraints that implement `FieldConstraint` and Message level constraints that implement `MessageConstraint`.
  
  
### FieldConstraint

Used to declare a constraint that a field on a message must adhere to.

### MessageConstraint

Used to declare a constraint that a message must adhere to.
  
## Predefined Constraints

```
field.violations.AssertFalse.message     = must be false
field.violations.AssertTrue.message      = must be true
field.violations.Future.message          = must be a future date
field.violations.Max.message             = must be less than or equal to {max}
field.violations.Min.message             = must be greater than or equal to {min}
field.violations.Negative.message        = must be less than 0
field.violations.NegativeOrZero.message  = must be less than or equal to 0
field.violations.NotBlank.message        = must not be blank
field.violations.NotEmpty.message        = must not be empty
field.violations.IsSet.message           = must be set
field.violations.IsNotSet.message        = must not be set
field.violations.Past.message            = must be a past date
field.violations.Pattern.message         = must match '{regexp}'
field.violations.Positive.message        = must be greater than 0
field.violations.PositiveOrZero.message  = must be greater than or equal to 0
field.violations.Size.message            = size must be between {min} and {max}
field.violations.Length.message          = length must be between {min} and {max}
field.violations.HasKey.message          = must have key '{key}'

message.violations.OneOfIsSet.message    = one of {fields} must be set
```

## Custom Constraints

Custom constraints can be created by implementing either `FieldConstraint` or `MessageConstraint`.

Some convenience abstract types can be extended which automatically performs the unwrapping of certain proto message types:

- `AbstractBooleanConstraint` (auto unwraps `BoolValue` or null if not set)
- `AbstractNumberConstraint` (auto unwraps `DoubleValue`, `FloatValue`, `Int32Value`, and `Int64Value` or null if not set)
- `AbstractStringConstraint` (auto unwraps `StringValue` or null if not set)
- `AbstractTimestampConstraint` (converts `Timestamp` to `Instant` or null if not set)
- `AbstractRepeatedConstraint` (converts repeated field to `List` type)
- `AbstractMapConstraint` (converts map field to `Map` type)

### Custom Constraint Violation Messages

Messages for custom constraints can be defined in `src/resources/ProtobufValidationMessages.properties`. You may also override predefined constraint messages here.

## Conditional Constraints

Conditionally applying a constraint is supported via the `ApplyCondition` interface.

### Usage of Conditional Constraints

```
                .addFieldConstraint(
                    "buzzerCode",
                    FieldConstraints.IS_SET,
                    ApplyConditions.when(ApplyConditions.fieldIsTrue("hasBuzzer")))
```

### Predefined ApplyConditions

```
constraint.condition.Expression.message  = when {expression}
constraint.condition.FieldIsTrue.message = when '{field}' is true
constraint.condition.FieldIsNotTrue.message = when '{field}' is not true
constraint.condition.FieldIsEqualTo.message = when '{field}' is equal to {value}
```

### Custom ApplyConditions

Custom ApplyConditions can be created by implementing the `ApplyCondition` interface.

Some convenience abstract types can be extended to provide additional support:

- `AbstractBooleanFieldCondition` (applies when a boolean field on the message has some value)
- `AbstractFieldCondition` (applies when some field on the message has some value)

### Violations

Constraint violations are determined by calling the following on `ProtobufValidator`:

```
public List<MessageViolation> validate(Message message);
```

Where `MessageViolation` looks like:

```
public class MessageViolation {

  private String path;
  private ErrorMessage error;
  private Optional<ConditionMessage> condition;
  private String defaultMessage;
  @Nullable private Object value;
  
  //Constructor and Getters ...

  public class ErrorMessage {
    private String code;
    private Map<String, Object> params;
  
    //Constructor and Getters ...
  }

  public class ConditionMessage {
    private String code;
    private Map<String, Object> params;
  
    //Constructor and Getters ...
    
  }
}
```
