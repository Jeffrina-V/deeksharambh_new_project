const mongoose = require('mongoose');

/**
 * Connects to MongoDB Atlas using the MONGO_URI environment variable.
 */
const connectDB = async () => {
    try {
        const connString = process.env.MONGO_URI;
        if (!connString) {
            console.error('Error: MONGO_URI environment variable is not defined in your .env file.');
            process.exit(1);
        }
        
        const options = {
            dbName: 'deeksharambh'
        };
        await mongoose.connect(connString, options);
        console.log('MongoDB Connected successfully to Atlas Cluster!');
    } catch (err) {
        console.error(`Database Connection Error: ${err.message}`);
        process.exit(1);
    }
};

module.exports = connectDB;
