package com.stratio.crossdata.common.data;

public class NodeName extends FirstLevelName {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 4037037885060672489L;

    /**
     * Connector name.
     */
    private final String name;

    /**
     * Constructor.
     * @param nodeName Connector Name.
     */
    public NodeName(String nodeName) {
        super();
        this.name = nodeName.toLowerCase();
    }

    /**
     * Get the Node Name.
     * @return Node Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the qualified name of the node.
     * @return qualified name of the node.
     */
    public String getQualifiedName() {
        return QualifiedNames.getNodeQualifiedName(getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NameType getType() {
        return NameType.NODE;
    }

}
