import React from 'react';

const HomeContainer = props => {
    console.log(props);
    const styles = {
        aTag: {
            cursor: 'pointer',
            color: 'red'
        }
    };


    return (
        <div>
            <div className="body">
            <p>TreePLE allows to manage trees in an urban environment. You can help by
                <a style={styles.aTag} onClick={() => props.history.push('/uploadFile')}> clicking here </a>
                to upload a csv file of trees.
                You can also view the current tree database by
                <a style={styles.aTag} onClick={() => props.history.push('/listAllTrees')}> clicking here</a>.
            </p>
            </div>
        </div>
    );
};

export default HomeContainer;